package org.skyline.core.handler.fetcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.skyline.common.data.ApplicationData;
import org.skyline.common.data.CounterData;
import org.skyline.common.data.HandlerResult;
import org.skyline.common.data.HandlerStatus;
import org.skyline.common.data.Records;
import org.skyline.common.data.tez.TezApplicationData;
import org.skyline.common.data.tez.TezDAG;
import org.skyline.common.data.tez.TezEdge;
import org.skyline.common.data.tez.TezTask;
import org.skyline.common.data.tez.TezTaskAttempt;
import org.skyline.common.data.tez.TezVertex;
import org.skyline.core.http.HadoopHACallService;
import org.skyline.core.storage.IStorage;
import org.skyline.core.utils.SkylineUtils;
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
  private static final String TEZ_VERTEX_URL = "/ws/v1/timeline/TEZ_VERTEX_ID?primaryFilter=TEZ_DAG_ID:%s&limit=%s";
  private static final String TEZ_TASK_URL = "/ws/v1/timeline/TEZ_TASK_ID?primaryFilter=TEZ_DAG_ID:%s&limit=%s";
  private static final String TEZ_TASK_ATTEMPT_URL = "/ws/v1/timeline/TEZ_TASK_ATTEMPT_ID?primaryFilter=TEZ_DAG_ID:%s&limit=%s";

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
  private IStorage storage;

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
      // DAG data & inited vertices info
      url = this.getTezDagUrl(applicationId);
      fetchedData = getDataFromTLS(url);
      TezDAG tezDAG = parseDag(fetchedData);
      tezData.setDag(tezDAG);

      // Application data, parse config here
      url = this.getTezApplicationUrl(applicationId);
      fetchedData = getDataFromTLS(url);
      tezData.setConf(parseConfFromJson(fetchedData));

      // Tez vertex
      String dagId = tezDAG.getDagId();

      url = this.getTezVertexUrl(dagId);
      fetchedData = getDataFromTLS(url);
      parseRuntimeVerticesInfo(fetchedData, tezDAG.getVertices());

      // Tez tasks
      url = this.getTezTaskUrl(dagId);
      fetchedData = getDataFromTLS(url);
      parseVertexTaskInfo(fetchedData, tezDAG.getVertices());

      // Tez task attempts
      url = this.getTezTaskAttemptUrl(dagId);
      fetchedData = getDataFromTLS(url);
      parseVertexTaskAttemptInfo(fetchedData, tezDAG.getVertices());

      tezData.setId(applicationId);
      if (saveApplicationData) {
        storage.upsert(ApplicationData.INDEX_NAME, ApplicationData.TYPE_NAME,
            Records.fromObject(tezData));
      }
      log.info("End fetch Tez application data for {}", applicationId);
    } catch (IOException e) {
      log.error("Error fetch TEZ application data, url : {}", url, e);
    }
    result.setApplicationData(tezData);
    result.setHandlerStatus(HandlerStatus.SUCCEEDED);
    return result;
  }

  private void parseVertexTaskAttemptInfo(JSONObject fetchedData, Map<String, TezVertex> vertices) {
    JSONArray entities = fetchedData.getJSONArray(ENTITIES_KEY);
    entities.forEach(x -> {
      JSONObject entity = (JSONObject) x;
      String attemptId = entity.getString(ENTITY_KEY);
      String vertexId = entity.getJSONObject("primaryfilters").getJSONArray("TEZ_VERTEX_ID")
          .toJavaList(String.class).get(0);
      String taskId = entity.getJSONObject("primaryfilters").getJSONArray("TEZ_TASK_ID")
          .toJavaList(String.class).get(0);
      TezTask task = vertices.get(vertexId).getTaskByTaskId(taskId);

      TezTaskAttempt attempt = entity.getObject(OTHER_INFO_KEY, TezTaskAttempt.class);
      attempt.setAttemptId(attemptId);
      if (task != null) {
        task.addAttempt(attempt);
      }
    });
  }

  private void parseVertexTaskInfo(JSONObject fetchedData, Map<String, TezVertex> vertices) {
    JSONArray entities = fetchedData.getJSONArray(ENTITIES_KEY);
    entities.forEach(x -> {
      JSONObject entity = (JSONObject) x;
      String taskId = entity.getString(ENTITY_KEY);
      String vertexId = entity.getJSONObject("primaryfilters").getJSONArray("TEZ_VERTEX_ID")
          .toJavaList(String.class).get(0);
      TezTask task = entity.getObject(OTHER_INFO_KEY, TezTask.class);
      task.setTaskId(taskId);
      if (task.getCounters() != null) {
        task.setCounters(
            parseCountersFromJson(entity.getJSONObject(OTHER_INFO_KEY).getJSONObject("counters")));
      } else {
        task.setCounters(CounterData.EMPTY_COUNTER);
      }
      TezVertex vertex = vertices.get(vertexId);
      vertex.addTask(task);
    });
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
    JSONObject otherInfo = dagJson.getJSONObject(OTHER_INFO_KEY);

    TezDAG dag = new TezDAG();
    dag.setDagId(dagJson.getString(ENTITY_KEY));
    dag.setDagName(otherInfo.getJSONObject("dagPlan").getString("dagName"));
    dag.setInitTime(otherInfo.getLongValue("initTime"));
    dag.setStartTime(otherInfo.getLongValue("startTime"));
    dag.setEndTime(otherInfo.getLongValue("endTime"));
    dag.setStatus(otherInfo.getString("status"));
    dag.setCounters(parseCountersFromJson(otherInfo.getJSONObject("counters")));

    // parse vertex & edge info when inited
    parseBasicVertexAndEdge(otherInfo, dag);

    return dag;
  }

  /**
   * Parse Basic Vertex & Edge info in dag response
   *
   * This is generated when dag inited
   */
  private void parseBasicVertexAndEdge(JSONObject otherInfo, TezDAG dag) {

    List<TezVertex> vertices = otherInfo.getJSONObject("dagPlan").getJSONArray("vertices")
        .toJavaList(TezVertex.class);

    List<TezEdge> edges = otherInfo.getJSONObject("dagPlan").getJSONArray("edges")
        .toJavaList(TezEdge.class);

    JSONObject vertexNameIdMapping = otherInfo.getJSONObject("vertexNameIdMapping");
    vertices.forEach(x -> x.setVertexId(vertexNameIdMapping.getString(x.getVertexName())));

    dag.setVertices(vertices.stream().collect(Collectors.toMap(TezVertex::getVertexId, v -> v)));
    dag.setEdges(edges.stream().collect(Collectors.toMap(TezEdge::getEdgeId, e -> e)));

  }

  /**
   * Parse runtime vertex info & merge it to basic vertex info
   */
  private void parseRuntimeVerticesInfo(JSONObject fetchedData, Map<String, TezVertex> vertices) {
    JSONArray entities = fetchedData.getJSONArray(ENTITIES_KEY);
    entities.forEach(x -> {
      JSONObject entity = (JSONObject) x;
      String vertexId = entity.getString(ENTITY_KEY);
      TezVertex vertex = entity.getObject(OTHER_INFO_KEY, TezVertex.class);
      if (vertex.getCounters() != null) {
        vertex.setCounters(
            parseCountersFromJson(entity.getJSONObject(OTHER_INFO_KEY).getJSONObject("counters")));
      } else {
        vertex.setCounters(CounterData.EMPTY_COUNTER);
      }
      SkylineUtils.copyPropertiesIgnoreNull(vertex, vertices.get(vertexId));
    });
  }

  private CounterData parseCountersFromJson(JSONObject counters) {
    if (counters == null) {
      return CounterData.EMPTY_COUNTER;
    }
    CounterData counterData = new CounterData();
    counters.getJSONArray("counterGroups").forEach(x -> {
      JSONObject counterGroup = (JSONObject) x;
      String counterGroupName = counterGroup.getString("counterGroupName");
      counterGroup.getJSONArray("counters").forEach(y -> {
        JSONObject counter = (JSONObject) y;
        counterData.setCounter(counterGroupName, counter.getString("counterName"),
            counter.getLong("counterValue"));
      });
    });
    return counterData;
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
    return String.format(TEZ_VERTEX_URL, dagId, maxTaskSize);
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
