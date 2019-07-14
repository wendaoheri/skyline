package org.dayu.core.handler.fetcher;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dayu.common.data.ApplicationData;
import org.dayu.core.handler.ApplicationInfoFetcher;
import org.dayu.core.handler.DisplayMessage;
import org.dayu.core.handler.HandlerStatus;
import org.dayu.core.http.HadoopHACallService;
import org.dayu.core.http.HttpCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-13
 */
@Component("mrFetcher")
@Slf4j
@ConfigurationProperties("org.dayu.core.handler.fetcher.mr-fetcher")
public class MRFetcher implements ApplicationInfoFetcher {

  @Setter
  private String ahsAddress;

  @Autowired
  private HadoopHACallService callService;

  private static final String JOB_INFO_URL = "/ws/v1/history/mapreduce/jobs/%s";
  private static final String JOB_CONF_URL = "/ws/v1/history/mapreduce/jobs/%s/conf";
  private static final String JOB_COUNTER_URL = "/ws/v1/history/mapreduce/jobs/%s/counters";
  private static final String TASK_LIST_INFO_URL = "/ws/v1/history/mapreduce/jobs/%s/tasks";
  private static final String TASK_INFO_URL = "/ws/v1/history/mapreduce/jobs/%s/tasks/%s";
  private static final String TASK_COUNTER_URL = "/ws/v1/history/mapreduce/jobs/%s/tasks/%s/counters";
  private static final String TASK_ATTEMPT_LIST_INFO_URL = "/ws/v1/history/mapreduce/jobs/%/tasks/%s/attempts";
  private static final String TASK_ATTEMPT_INFO_URL = "/ws/v1/history/mapreduce/jobs/%s/tasks/%s/attempt/%s";
  private static final String TASK_ATTEMPT_COUNTER_URL = "/ws/v1/history/mapreduce/jobs/%s/tasks/%s/attempt/%s/counters";

  @Override
  public HandlerStatus handle(ApplicationData applicationData) {
    String applicationId = applicationData.getApplicationId();
    String jobId = this.getJobIdFromApplicationId(applicationId);
    log.info("got Application data : {}", applicationData);
    return null;
  }

  private String getJobIdFromApplicationId(String applicationId) {
    return applicationId.replace("application", "job");
  }

  private String getJobInfoUrl(String jobId) {

    return null;
  }

  @Override
  public DisplayMessage display() {
    return null;
  }
}
