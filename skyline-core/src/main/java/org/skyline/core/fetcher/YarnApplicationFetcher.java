package org.skyline.core.fetcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.skyline.common.data.YarnApplication;
import org.skyline.core.http.HadoopHACallService;
import org.skyline.core.service.YarnApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author sean
 */
@Service
@Slf4j
public class YarnApplicationFetcher {

  private static final String YARN_APPLICATION_LIST_STARTED_URL = "/ws/v1/cluster/apps?startedTimeBegin=%d&startedTimeEnd=%d";
  private static final String YARN_APPLICATION_LIST_FINISHED_URL = "/ws/v1/cluster/apps?finishedTimeBegin=%d&finishedTimeEnd=%d";
  private static final String YARN_RUNNING_APPLICATION_LIST_URL = "/ws/v1/cluster/apps?states=NEW,NEW_SAVING,SUBMITTED,ACCEPTED,RUNNING";


  @Autowired
  private HadoopHACallService hadoopHACallService;

  @Autowired
  private YarnApplicationService yarnApplicationService;

  @Value("${hadoop.resourceManagerAddress}")
  private String resourceManagerAddress;

  @Value("${skyline.fetcher.interval_second}")
  private int fetchInterval;

  @Value("${skyline.fetcher.unfinished_interval_second}")
  private int beforeRunningFetchInterval;

  @Scheduled(fixedRate = 60000)
  public void fetch() {
    long begin = yarnApplicationService.getLastFetchTime();
    long end = System.currentTimeMillis();
    log.info("Fetch application window : {} to {}", begin, end);

    List<YarnApplication> apps = Lists.newArrayList();

    apps.addAll(this.fetchApplications(begin, end, 0));
    apps.addAll(this.fetchApplications(begin, end, 1));

    yarnApplicationService.saveApplicationList(apps);
    yarnApplicationService.sendApplicationListToMQ(apps);

    yarnApplicationService.setLastFetchTime(end);

    log.info("Fetch application list finished");
  }


  /**
   * fetch application list between window
   *
   * @param begin timeBegin
   * @param end timeEnd
   * @param startedOrFinished 0: started, 1: finished
   */
  private List<YarnApplication> fetchApplications(long begin, long end, int startedOrFinished) {
    String fetchAppListUrl = this.getAppListUrl(begin, end, startedOrFinished);
    log.info("Fetch application url is : {}", fetchAppListUrl);
    List<YarnApplication> apps = null;
    String resp = null;
    try {
      resp = hadoopHACallService.doGet(resourceManagerAddress, fetchAppListUrl);
      apps = parseRespToYarnApplicationList(resp);
      log.info("Fetched app size : {} startedOrFinished : {}", apps == null ? 0 : apps.size(),
          startedOrFinished);
    } catch (IOException e) {
      log.error("Error occur while fetch yarn application list {}", e.getMessage());
    } catch (Exception e) {
      log.info("Response is : {}", resp);
      log.error("Unexpected error occur while fetch yarn application list.", e);
    }
    return apps != null ? apps : Lists.newArrayList();
  }

  /**
   * fetch application list current in following handlerStatus
   *
   * NEW, NEW_SAVING, SUBMITTED, ACCEPTED, RUNNING
   */
  @Scheduled(fixedRate = 10000)
  public void fetchUnfinishedApplications() {
    String runningAppListUrl = this.getRunningAppListUrl();
    try {
      log.info("Fetch unfinished application url is : {}", runningAppListUrl);
      String resp = hadoopHACallService.doGet(resourceManagerAddress, runningAppListUrl);
      List<YarnApplication> apps = parseRespToYarnApplicationList(resp);

      log.info("Got unfinished application list size : {}", apps.size());

      apps.forEach(x -> x.setId(null));

      yarnApplicationService.saveApplicationListTrace(apps);

    } catch (IOException e) {
      log.error("Error occur while fetch yarn application list {}", e.getMessage());
    }


  }

  @VisibleForTesting
  protected List<YarnApplication> parseRespToYarnApplicationList(String resp) {
    JSONObject jo = JSON.parseObject(resp);
    List<YarnApplication> apps = null;
    try {
      apps = jo.getJSONObject("apps").getJSONArray("app")
          .toJavaList(YarnApplication.class);
      apps.forEach(x -> x.setApplicationId(x.getId()));
    } catch (NullPointerException e) {
      log.info("None application list");
    }
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
          .format(YARN_APPLICATION_LIST_STARTED_URL, begin, end);
    } else {
      return String
          .format(YARN_APPLICATION_LIST_FINISHED_URL, begin, end);
    }

  }

  protected String getRunningAppListUrl() {
    return YARN_RUNNING_APPLICATION_LIST_URL;
  }
}
