package org.skyline.core.handler.fetcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.skyline.common.data.ApplicationData;
import org.skyline.common.data.HandlerResult;
import org.skyline.common.data.tez.TezApplicationData;
import org.skyline.common.data.tez.TezDAG;
import org.skyline.core.http.HadoopHACallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-13
 */
@Component("tezFetcher")
@Slf4j
public class TezFetcher extends ApplicationInfoFetcher {

  private static final String TEZ_DAG_URL = "/ws/v1/timeline/TEZ_DAG_ID?primaryFilter=applicationId:%s";
  private static final String TEZ_APPLICATION_URL = "/ws/v1/timeline/TEZ_APPLICATION/tez_%s";
  private static final String TEZ_VERTEX_URL = "/ws/v1/timeline/TEZ_VERTEX_ID?primaryFilter=TEZ_DAG_ID:%s&limits=%s";
  private static final String TEZ_TASK_URL = "/ws/v1/timeline/TEZ_TASK_ID?primaryFilter=TEZ_DAG_ID:%s&limits=%s";
  private static final String TEZ_TASK_ATTEMPT_URL = "/ws/v1/timeline/TEZ_TASK_ATTEMPT_ID?primaryFilter=TEZ_DAG_ID:%s&limits=%s";

  private static final String ENTITIES_KEY = "entities";
  private static final String ENTITY_KEY = "entity";
  private static final String OTHER_INFO_KEY = "otherinfo";

  @Value("${hadoop.timelineServerAddress}")
  private String tlsAddress;

  @Value("${skyline.handler.fetcher.tez.save_data:false}")
  private boolean saveApplicationData;

  @Value("${skyline.handler.fetcher.tez.max_task_size:10000}")
  private int maxTaskSize;

  @Autowired
  private HadoopHACallService callService;

  @Override
  public HandlerResult handle(ApplicationData applicationData) {
    HandlerResult result = new HandlerResult();

    TezApplicationData tezData = (TezApplicationData) applicationData;
    String applicationId = tezData.getApplicationId();
    log.info("Start fetch Tez application data for {}", applicationId);

    String url = null;
    JSONObject fetchedData;
    try {
      // DAG data
      url = this.getTezDagUrl(applicationId);
      fetchedData = getDataFromTLS(url);
      TezDAG tezDAG = parseDag(fetchedData);
      tezData.setDag(tezDAG);

      // Application data, parse config here
      url = this.getTezApplicationUrl(applicationId);
      fetchedData = getDataFromTLS(url);
      tezData.setConf(parseConfFromJson(fetchedData));

      String dagId = tezDAG.getDagId();

      // Tez vertex


    } catch (IOException e) {
      log.error("Error fetch TEZ application data, url : {}", url, e);
    }

    return null;
  }

  private Properties parseConfFromJson(JSONObject fetchedData) {
    Properties props = new Properties();
    JSONObject configJSON = fetchedData.getJSONObject(OTHER_INFO_KEY).getJSONObject("config");
    for (Entry<String, Object> entry : configJSON.entrySet()) {
      props.put(entry.getKey().replace('.', '_'), entry.getValue());
    }
    return props;
  }

  private TezDAG parseDag(JSONObject fetchedData) {
    JSONObject dagJson = getLastEntity(fetchedData);
    return null;
  }

  /**
   * Get last entity in entities, sort by entity key asc
   */
  private JSONObject getLastEntity(JSONObject json) {
    JSONArray entities = json.getJSONArray(ENTITIES_KEY);
    entities.sort((x, y) -> {
      JSONObject jX = (JSONObject) x;
      JSONObject jY = (JSONObject) y;
      return jX.getString(ENTITY_KEY).compareTo(jY.getString(ENTITY_KEY));
    });
    return entities.getJSONObject(entities.size() - 1);
  }

  @VisibleForTesting
  JSONObject getDataFromTLS(String url) throws IOException {
    String resp = callService.doGet(tlsAddress, url);
    return JSON.parseObject(resp);
  }

  @VisibleForTesting
  String getTezDagUrl(String applicationId) {
    return String.format(TEZ_DAG_URL, applicationId);
  }

  @VisibleForTesting
  String getTezApplicationUrl(String applicationId) {
    return String.format(TEZ_APPLICATION_URL, applicationId);
  }

  @VisibleForTesting
  String getTezVertexUrl(String dagId) {
    return String.format(TEZ_VERTEX_URL, dagId);
  }

  @VisibleForTesting
  String getTezTaskUrl(String dagId) {
    return String.format(TEZ_TASK_URL, dagId, maxTaskSize);
  }

  @VisibleForTesting
  String getTezTaskAttemptUrl(String dagId) {
    return String.format(TEZ_TASK_ATTEMPT_URL, dagId, maxTaskSize);
  }

}
