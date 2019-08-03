package org.dayu.core.handler.fetcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.HandlerResult;
import org.dayu.common.data.HandlerStatus;
import org.dayu.common.data.Records;
import org.dayu.common.data.mr.JobAttemptData;
import org.dayu.common.data.mr.JobData;
import org.dayu.common.data.mr.MRApplicationData;
import org.dayu.common.data.mr.MRCounterData;
import org.dayu.common.data.mr.MRTaskData;
import org.dayu.common.data.mr.TaskAttemptData;
import org.dayu.core.handler.ApplicationInfoFetcher;
import org.dayu.core.http.HadoopHACallService;
import org.dayu.core.storage.IStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-13
 */
@SuppressWarnings("ALL")
@Component("mrFetcher")
@Slf4j
public class MRFetcher implements ApplicationInfoFetcher {

  private static final String JOB_INFO_URL = "/ws/v1/history/mapreduce/jobs/%s";
  private static final String JOB_CONF_URL = "/ws/v1/history/mapreduce/jobs/%s/conf";
  private static final String JOB_ATTEMPTS_URL = "/ws/v1/history/mapreduce/jobs/%s/jobattempts";
  private static final String JOB_COUNTER_URL = "/ws/v1/history/mapreduce/jobs/%s/counters";
  private static final String JOB_TASKS_URL = "/ws/v1/history/mapreduce/jobs/%s/tasks";
  private static final String TASK_COUNTER_URL = "/ws/v1/history/mapreduce/jobs/%s/tasks/%s/counters";
  private static final String TASK_ATTEMPT_LIST_INFO_URL = "/ws/v1/history/mapreduce/jobs/%s/tasks/%s/attempts";

  @Value("${hadoop.historyServerAddress}")
  private String ahsAddress;

  @Autowired
  private HadoopHACallService callService;

  @Autowired
  private IStorage storage;

  @Override
  public HandlerResult handle(ApplicationData applicationData) {
    HandlerResult result = new HandlerResult();

    MRApplicationData mrData = (MRApplicationData) applicationData;
    String applicationId = applicationData.getApplicationId();
    String jobId = this.getJobIdFromApplicationId(applicationId);
    try {
      // fetch job info
      JSONObject jo = getDataFromAHS(getJobInfoUrl(jobId));
      JobData jobData = jo.getObject("job", JobData.class);
      mrData.setJobData(jobData);

      // fetch job config
      jo = getDataFromAHS(getJobConfUrl(jobId));
      Properties conf = parseConfFromJson(jo);
      mrData.setConf(conf);

      // fetch job attempts
      jo = getDataFromAHS(getJobAttemptsUrl(jobId));
      List<JobAttemptData> jobAttemptDataList = jo.getJSONObject("jobAttempts")
          .getJSONArray("jobAttempt").toJavaList(JobAttemptData.class);
      mrData.setJobAttemptDataList(jobAttemptDataList);

      // fetch job counter
      jo = getDataFromAHS(getJobCounterUrl(jobId));
      MRCounterData jobCounterData = parseJobCounterFromJson(jo);
      mrData.setJobCounterData(jobCounterData);

      // fetch job tasks
      jo = getDataFromAHS(getJobTasksUrl(jobId));
      List<MRTaskData> mrTaskDataList = jo.getJSONObject("tasks").getJSONArray("task")
          .toJavaList(MRTaskData.class);
      mrData.setTaskDataList(mrTaskDataList);

      // fetch task level info
      for (MRTaskData taskData : mrTaskDataList) {
        String taskId = taskData.getId();

        // fetch task counter
        jo = getDataFromAHS(getTaskCounterUrl(jobId, taskId));
        MRCounterData taskCounterData = parseTaskCounterFromJson(jo);
        taskData.setTaskCounterData(taskCounterData);

        // fetch task attempts
        jo = getDataFromAHS(getTaskAttemptsUrl(jobId, taskId));
        List<TaskAttemptData> taskAttemptDataList = jo.getJSONObject("taskAttempts")
            .getJSONArray("taskAttempt").toJavaList(TaskAttemptData.class);
        taskData.setTaskAttemptDataList(taskAttemptDataList);
      }
      // set id & save to es
      mrData.setId(applicationId);
      storage.upsert(ApplicationData.DATABASE_NAME, ApplicationData.TABLE_NAME,
          Records.fromObject(mrData));

      result.setApplicationData(mrData);
      result.setHandlerStatus(HandlerStatus.SUCCESSED);
    } catch (IOException e) {
      log.error("MapReduce job info fetch failed", e);
      result.setHandlerStatus(HandlerStatus.FAILED);
    }
    return result;
  }

  private JSONObject getDataFromAHS(String url) throws IOException {
    String resp = callService.doGet(ahsAddress, url);
    return JSON.parseObject(resp);
  }

  private String getJobIdFromApplicationId(String applicationId) {
    return applicationId.replace("application", "job");
  }

  private String getJobInfoUrl(String jobId) {
    return String.format(JOB_INFO_URL, jobId);
  }

  private String getJobConfUrl(String jobId) {
    return String.format(JOB_CONF_URL, jobId);
  }

  private String getJobAttemptsUrl(String jobId) {
    return String.format(JOB_ATTEMPTS_URL, jobId);
  }

  private String getJobCounterUrl(String jobId) {
    return String.format(JOB_COUNTER_URL, jobId);
  }

  private String getJobTasksUrl(String jobId) {
    return String.format(JOB_TASKS_URL, jobId);
  }

  private String getTaskCounterUrl(String jobId, String taskId) {
    return String.format(TASK_COUNTER_URL, jobId, taskId);
  }

  private String getTaskAttemptsUrl(String jobId, String taskId) {
    return String.format(TASK_ATTEMPT_LIST_INFO_URL, jobId, taskId);
  }

  private Properties parseConfFromJson(JSONObject jo) {
    Properties props = new Properties();
    JSONArray ja = jo.getJSONObject("conf").getJSONArray("property");
    for (int i = 0; i < ja.size(); i++) {
      JSONObject conf = ja.getJSONObject(i);
      props.setProperty(conf.getString("name"), conf.getString("value"));
    }
    return props;
  }

  private MRCounterData parseJobCounterFromJson(JSONObject jo) {
    MRCounterData data = new MRCounterData();
    List<JSONObject> groups = jo.getJSONObject("jobCounters").getJSONArray("counterGroup")
        .toJavaList(JSONObject.class);
    parseCounterValue("totalCounterValue", data, groups);
    return data;
  }

  private MRCounterData parseTaskCounterFromJson(JSONObject jo) {
    MRCounterData data = new MRCounterData();
    List<JSONObject> groups = jo.getJSONObject("jobTaskCounters").getJSONArray("taskCounterGroup")
        .toJavaList(JSONObject.class);
    parseCounterValue("value", data, groups);
    return data;
  }

  private void parseCounterValue(String valueName, MRCounterData data, List<JSONObject> groups) {
    for (JSONObject group : groups) {
      String counterGroupName = group.getString("counterGroupName");
      List<JSONObject> counters = group.getJSONArray("counter").toJavaList(JSONObject.class);
      for (JSONObject counter : counters) {
        String name = counter.getString("name");
        long value = counter.getLongValue(valueName);
        data.setCounter(counterGroupName, name, value);
      }
    }
  }

}
