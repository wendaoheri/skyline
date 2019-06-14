package org.dayu.plugin.schedule.dsp;

import com.google.common.collect.Sets;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.dayu.plugin.schedule.SchedulePlugin;
import org.dayu.plugin.schedule.ScheduleTrigger;
import org.dayu.plugin.schedule.dsp.model.JobApplicationLog;
import org.dayu.plugin.schedule.dsp.repository.JobApplicationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Sean Liu
 * @date 2019-04-02
 */
@Slf4j
public class DSPSchedulePlugin implements SchedulePlugin {


  private static final String LOG_SPLIT = ",";
  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd0000");

  @Autowired
  private DSPScheduleCache cache;

  @Autowired
  private JobApplicationLogRepository jobApplicationLogRepository;

  private Date last;

  @Override
  public ScheduleTrigger getScheduleIdByApplicationId(String applicationId) {
    return cache.getScheduleIdByApplicationIdWithCache(applicationId);
  }

  @Scheduled(fixedRate = 10000)
  public void refresh() {
    log.info("start refresh caches");
    Date curr = new Date(System.currentTimeMillis() - 60000L);
    if(last == null) {
      last = new Date(System.currentTimeMillis() - 86400000L);
    }
    log.info("start last fetch schedule time : {}", last);
    List<JobApplicationLog> jobs = jobApplicationLogRepository
        .findJobApplicationLogsByCreatedTimeGreaterThanEqual(last);
    putCaches(jobs);
    last = curr;
    log.info("end refresh caches");
  }

  public void putCaches(List<JobApplicationLog> jobApplicationLogs) {
    log.info("Start add to cache application size : {}", jobApplicationLogs.size());
    jobApplicationLogs.forEach(job -> {

      String jobId = String.valueOf(job.getJobId());
      String applicationLog = job.getApplicationLog();
      Long frequency = job.getJobFrequency();

      // 切割application_id，并且根据application_id从小到大排序
      Set<String> applicationIdSet = Sets.newHashSet(applicationLog.split(LOG_SPLIT));
      List<String> applicationIdList = applicationIdSet.stream().sorted(
          Comparator.comparing(o -> Long.valueOf(o.split("_")[2])))
          .collect(Collectors.toList());

      for (int i = 0; i < applicationIdList.size(); i++) {
        String applicationId = applicationIdList.get(i);
        ScheduleTrigger st = new ScheduleTrigger();

        // 给每个schedule里面的yarn job按照 application_id从小到大打上虚拟的job_id
        st.setScheduleId(jobId);
        st.setTriggerId(String.valueOf(frequency));
        st.setJobId(String.format("%s_%s", jobId, i + 1));

        if (cache.getScheduleIdByApplicationIdWithCache(applicationId) == null) {
          cache.putCache(applicationId, st);
          log.info(cache.getScheduleIdByApplicationIdWithCache(applicationId).toString());
        }
      }
    });
    log.info("End add to cache application size : {}", jobApplicationLogs.size());
  }

}
