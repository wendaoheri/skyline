package org.dayu.core.storage.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.dayu.common.model.Record;
import org.dayu.core.storage.IStorage;
import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ESStorage implements IStorage {

  private static final String KEY_ID = "_id";
  private static final String KEY_ROUTE = "_route";
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

  @Override
  public void bulkUpsert(String indexName, String typeName, Collection<Record> records)
      throws ExecutionException, InterruptedException {
    if (records == null || records.size() == 0) {
      return;
    }
    indexName = indexName.toLowerCase();
    typeName = typeName.toLowerCase();

    Iterator<Record> iter = records.iterator();
    Record item;
    BulkRequestBuilder bulkRequestBuilder = null;

    int batchSize = 1000;
    int index = 0;
    while (iter.hasNext()) {
      if (index % batchSize == 0) {
        bulkRequestBuilder = client.prepareBulk();
      }
      item = iter.next();

      ActionRequestBuilder builder = this.getActionBuilder(indexName, typeName, item);

      if (builder instanceof UpdateRequestBuilder) {
        bulkRequestBuilder.add((UpdateRequestBuilder) builder);
      } else if (builder instanceof IndexRequestBuilder) {
        bulkRequestBuilder.add((IndexRequestBuilder) builder);
      } else {
        throw new ExecutionException("Unknown operation", new Exception());
      }

      index++;
      if (index % batchSize == 0) {
        BulkResponse responses = bulkRequestBuilder.execute().get();
        bulkRequestBuilder = null;
        if (responses.hasFailures()) {
          throw new ExecutionException(responses.buildFailureMessage(), new Exception());
        }
      }
    }

    if (bulkRequestBuilder != null) {
      BulkResponse responses = bulkRequestBuilder.execute().get();
      if (responses.hasFailures()) {
        throw new ExecutionException(responses.buildFailureMessage(), new Exception());
      }
    }
  }

  @Override
  public void upsert(String indexName, String typeName, Record record) {
    ActionRequestBuilder builder = this.getActionBuilder(indexName, typeName, record);
    builder.get();
  }

  private ActionRequestBuilder getActionBuilder(String indexName, String typeName, Record record) {
    record.put("@timestamp", System.currentTimeMillis());
    if (record.containsKey(KEY_ID) && record.get(KEY_ID) != null) {
      UpdateRequestBuilder builder = client
          .prepareUpdate(indexName, typeName, record.get(KEY_ID).toString());
      record.remove(KEY_ID);
      builder.setDoc(record);
      builder.setDocAsUpsert(true);
      return builder;
    } else {
      IndexRequestBuilder builder = client.prepareIndex(indexName, typeName);
      builder.setSource(record);
      return builder;
    }
  }

  @Override
  public <T> T findById(String indexName, String typeName, String id, Type clazz) {
    GetResponse response;
    try {
      response = client.prepareGet(indexName, typeName, id).get();
    } catch (IndexNotFoundException e) {
      return null;
    }
    JSONObject jo = JSON.parseObject(response.getSourceAsString());
    jo.put(KEY_ID, response.getId());
    return jo.toJavaObject(clazz);
  }

  @Override
  public <T> List<T> findByDSL(String indexName, String typeName, String dsl, Type clazz) {
    List<T> result = Lists.newArrayList();

    QueryBuilder query = QueryBuilders.wrapperQuery(dsl);
    SearchRequestBuilder builder = client.prepareSearch(indexName)
        .setTypes(typeName)
        .setFetchSource(true)
        .setSize(10000)
        .setQuery(query);

    SearchResponse response = builder.get();

    for (SearchHit hit : response.getHits().getHits()) {
      JSONObject jo = JSON.parseObject(hit.getSourceAsString());
      jo.put(KEY_ID, hit.getId());
      result.add(jo.toJavaObject(clazz));
    }
    return result;
  }

  @Override
  public <T> List<T> findByDSL(String indexName, String typeName, String dsl, Type clazz,
      String... fields) {

    QueryBuilder query = QueryBuilders.wrapperQuery(dsl);
    SearchRequestBuilder builder = fieldSearchBuilder(indexName, typeName, query,
        fields);
    SearchResponse response = builder.get();

    return parseResponse(response, clazz);
  }

  @Override
  public <T> List<T> findByIds(String indexName, String typeName, Set<String> ids, Type clazz,
      String... fields) {
    IdsQueryBuilder query = QueryBuilders.idsQuery();
    if (null != ids) {
      ids.forEach(x -> query.addIds(x));
    }
    SearchRequestBuilder builder = fieldSearchBuilder(indexName, typeName, query,
        fields);
    SearchResponse response = builder.get();
    return parseResponse(response, clazz);
  }

  private <T> List<T> parseResponse(SearchResponse response, Type clazz) {
    List<T> result = Lists.newArrayList();
    for (SearchHit hit : response.getHits().getHits()) {
      JSONObject jo = new JSONObject();
      Set<Entry<String, DocumentField>> entries = hit.getFields().entrySet();
      for (Entry<String, DocumentField> entry : entries) {
        jo.put(entry.getKey(), entry.getValue().getValue());
      }
      jo.put(KEY_ID, hit.getId());
      result.add(jo.toJavaObject(clazz));
    }
    return result;
  }

  private SearchRequestBuilder fieldSearchBuilder(String indexName, String typeName,
      QueryBuilder query, String... fields) {
    SearchRequestBuilder builder = client.prepareSearch(indexName)
        .setTypes(typeName)
        .setFetchSource(false)
        .setSize(10000)
        .setQuery(query);
    for (String field : fields) {
      builder.addDocValueField(field);
    }
    return builder;
  }

  private boolean indexExists(String indexName) {
    IndicesExistsRequestBuilder builder = client.admin().indices()
        .prepareExists(indexName);
    IndicesExistsResponse resp = builder.get();
    return resp.isExists();
  }

  public Map<String, Object> parseObj(Object obj) {
    String json = JSON.toJSONString(obj);
    JSONObject jo = JSON.parseObject(json);
    return jo;
  }
}