package org.dayu.plugin.storage.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.dayu.storage.IStorage;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ESStorage implements IStorage {

  @Autowired
  private TransportClient client;


  @Override
  public void trace(List<Object> data) {
    String indexName = String.format("dayu-%s", DateFormatUtils.format(new Date(), "yyyyMMdd"));
    BulkRequestBuilder bulkBuilder = client.prepareBulk();
    Long curr = System.currentTimeMillis();
    for (Object x : data) {
      Map<String, Object> source = parseObj(x);
      source.put("@timestamp", curr);
      IndexRequestBuilder indexReq = client.prepareIndex(indexName, "application_metrics")
          .setSource(source);
      bulkBuilder.add(indexReq);
    }
    BulkResponse bulkResponse = bulkBuilder.get();
    if (bulkResponse.hasFailures()) {
      log.error("Trace Log failed with error {}", bulkResponse.buildFailureMessage());
    } else {
      log.info("Success trace application size : [{}]", data.size());
    }
  }

  public Map<String, Object> parseObj(Object obj) {
    String json = JSON.toJSONString(obj);
    JSONObject jo = JSON.parseObject(json);
    return jo;
  }
}