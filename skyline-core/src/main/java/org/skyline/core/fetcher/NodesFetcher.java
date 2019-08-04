package org.skyline.core.fetcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.skyline.common.data.Node;
import org.skyline.common.data.Records;
import org.skyline.core.http.HadoopHACallService;
import org.skyline.core.storage.IStorage;
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
public class NodesFetcher {

  private static final String NODES_LIST_URL = "/ws/v1/cluster/nodes";

  @Autowired
  private IStorage storage;

  @Autowired
  private HadoopHACallService hadoopHACallService;

  @Value("${hadoop.resourceManagerAddress}")
  private String resourceManagerAddress;

  @Scheduled(fixedRate = 1000)
  public void fetch() {
    try {
      log.info("Fetch nodes list start");
      String resp = hadoopHACallService.doGet(resourceManagerAddress, NODES_LIST_URL);
      JSONObject jo = JSON.parseObject(resp);
      List<Node> nodes = jo.getJSONObject("nodes").getJSONArray("node").toJavaList(Node.class);
      log.info("Got nodes size : {}", nodes.size());
      if (nodes.size() > 0) {
        storage.bulkUpsert(Node.INDEX_NAME, Node.TYPE_NAME, Records.fromObject(nodes));
      }
      log.info("Fetch nodes list end");
    } catch (IOException e) {
      log.error("Fetch cluster node failed: {}", e);
    } catch (NullPointerException e) {
      log.error("None node list");
    } catch (InterruptedException | ExecutionException e) {
      log.error("Save node list error : {}", e);
    }
  }

}
