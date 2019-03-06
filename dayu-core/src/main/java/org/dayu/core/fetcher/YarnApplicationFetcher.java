package org.dayu.core.fetcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.dayu.common.http.HttpCallService;
import org.dayu.core.model.YarnApplication;
import org.dayu.core.service.YarnApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class YarnApplicationFetcher {

  public static final String YARN_APPLICATION_LIST_STARTED_URL_PATTERN = "%s/ws/v1/cluster/apps?startedTimeBegin=%d&startedTimeEnd=%d";
  public static final String YARN_APPLICATION_LIST_FINISHED_URL_PATTERN = "%s/ws/v1/cluster/apps?finishedTimeBegin=%d&finishedTimeEnd=%d";
  public static final String YARN_RUNNING_APPLICATION_LIST_URL_PATTERN = "%s/ws/v1/cluster/apps?states=NEW,NEW_SAVING,SUBMITTED,ACCEPTED,RUNNING";


  @Autowired
  private HttpCallService httpCallService;

  @Autowired
  private YarnApplicationService yarnApplicationService;


  @Value("${hadoop.resourceManagerAddress}")
  private String resourceManagerAddress;

  @Value("${dayu.fetcher.interval_time}")
  private int fetchInterval;

  @Value("${dayu.fetcher.before_running_interval_time}")
  private int beforeRunningFetchInterval;

  public void fetch() {
    long begin = yarnApplicationService.getLastFetchTime();
    long end = System.currentTimeMillis();
    log.info("Fetch application window : {} to {}", begin, end);

    List<YarnApplication> startedApps = this.fetchApplications(begin, end, 0);
    log.info("fetch started app list size: {}", startedApps.size());
    yarnApplicationService.saveApplicationList(startedApps);

    List<YarnApplication> finishedApps = this.fetchApplications(begin, end, 1);
    log.info("fetch finished app list size: {}", finishedApps.size());
    yarnApplicationService.saveApplicationList(finishedApps);

    yarnApplicationService.setLastFetchTime(end);
  }


  /**
   * fetch application list between window
   *
   * @param begin timeBegin
   * @param end timeEnd
   * @param startedOrFinished 0: started, 1: finished
   */
  public List<YarnApplication> fetchApplications(long begin, long end, int startedOrFinished) {
    String fetchAppListUrl = this.getAppListUrl(begin, end, startedOrFinished);
    log.info("Fetch started application url is : {}", fetchAppListUrl);
    List<YarnApplication> apps = null;
    try {
      String resp = httpCallService.doGet(fetchAppListUrl);
      apps = parseRespToYarnApplicationList(resp);
    } catch (IOException e) {
      log.error("Error occur while fetch yarn application list {}", e.getMessage());
    }
    return apps != null ? apps : Lists.newArrayList();
  }

  /**
   * fetch application list current in following status
   *
   * NEW, NEW_SAVING, SUBMITTED, ACCEPTED, RUNNING
   */
  public void fetchBeforeRunningApplications() {
    String runningAppListUrl = this.getRunningAppListUrl();
    try {
      String resp = httpCallService.doGet(runningAppListUrl);
      List<YarnApplication> apps = parseRespToYarnApplicationList(resp);
      log.info("Got before RUNNING application list size : {}", apps.size());
    } catch (IOException e) {
      log.error("Error occur while fetch yarn application list {}", e.getMessage());
    }


  }

  private List<YarnApplication> parseRespToYarnApplicationList(String resp) {
    JSONObject jo = JSON.parseObject(resp);
    List<YarnApplication> apps = jo.getJSONObject("apps").getJSONArray("app")
        .toJavaList(YarnApplication.class);
    return apps;
  }

  /**
   * compose url for fetch application list
   *
   * @param begin beginTime
   * @param end endTime
   * @param startedOrFinished 0: started, 1 finished
   */
  @VisibleForTesting
  protected String getAppListUrl(long begin, long end, int startedOrFinished) {
    if (startedOrFinished == 0) {
      return String
          .format(YARN_APPLICATION_LIST_STARTED_URL_PATTERN, resourceManagerAddress, begin, end);
    } else {
      return String
          .format(YARN_APPLICATION_LIST_FINISHED_URL_PATTERN, resourceManagerAddress, begin, end);
    }

  }

  protected String getRunningAppListUrl() {
    return String.format(YARN_RUNNING_APPLICATION_LIST_URL_PATTERN, resourceManagerAddress);
  }
}
