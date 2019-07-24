package org.dayu.core.fetcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.dayu.common.model.Records;
import org.dayu.common.model.YarnMetrics;
import org.dayu.core.http.HadoopHACallService;
import org.dayu.core.storage.IStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Sean Liu
 * @date 2019-07-22
 */
@Service
@Slf4j
public class YarnMetricsFetcher {

  private static final String CLUSTER_METRICS_URL = "/ws/v1/cluster/metrics";

  @Autowired
  private IStorage storage;

  @Autowired
  private HadoopHACallService hadoopHACallService;

  @Value("${hadoop.resourceManagerAddress}")
  private String resourceManagerAddress;

  @Scheduled(fixedRate = 1000)
  public void fetch() {
    try {
      log.info("Fetch metrics start");
      String resp = hadoopHACallService.doGet(resourceManagerAddress, CLUSTER_METRICS_URL);
      JSONObject jo = JSON.parseObject(resp);
      YarnMetrics metrics = jo.getObject("clusterMetrics", YarnMetrics.class);
      storage
          .upsert(YarnMetrics.DATABASE_NAME, YarnMetrics.TABLE_NAME, Records.fromObject(metrics));
      log.info("Fetch metrics end");
    } catch (IOException e) {
      log.error("Fetch yarn metrics failed : {}", e);
    }
  }

}