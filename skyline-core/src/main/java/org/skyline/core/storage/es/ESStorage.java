package org.skyline.core.storage.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
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
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.skyline.core.dto.Filter;
import org.skyline.core.dto.Order;
import org.skyline.core.dto.ScrolledPageResult;
import org.skyline.core.dto.SearchRequest;
import org.skyline.core.storage.IStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class ESStorage implements IStorage {

  public static final String KEY_ID = "_id";
  public static final String KEY_ROUTE = "_route";

  @Autowired
  private TransportClient client;

  @Value("${spring.data.elasticsearch.template-file}")
  private String esTemplateFile;

  @PostConstruct
  private void init() throws IOException {
    try {
      JSONObject templates = JSON
          .parseObject(this.getClass().getClassLoader().getResourceAsStream(esTemplateFile),
              JSONObject.class);
      log.info("Es template : {}", templates.toJSONString());
      for (String templateName : templates.keySet()) {
        JSONObject template = templates.getJSONObject(templateName);
        client.admin().indices().preparePutTemplate(templateName).setSource(template).get();
        log.info("put mapping for {} : {}", templateName, template);
      }

    } catch (IOException e) {
      log.error("Load es template from {} failed", esTemplateFile);
      throw e;
    }
  }

  @Override
  public void trace(List<Object> data) {
    String indexName = String.format("skyline-%s", DateFormatUtils.format(new Date(), "yyyyMMdd"));
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
  public void bulkUpsert(String indexName, String typeName, JSONArray records)
      throws ExecutionException, InterruptedException {
    if (records == null || records.size() == 0) {
      return;
    }
    indexName = indexName.toLowerCase();
    typeName = typeName.toLowerCase();

//    Iterator<Record> iter = records.iterator();
    Iterator<Object> iter = records.iterator();
    JSONObject item;
    BulkRequestBuilder bulkRequestBuilder = null;

    int batchSize = 1000;
    int index = 0;
    while (iter.hasNext()) {
      if (index % batchSize == 0) {
        bulkRequestBuilder = client.prepareBulk();
      }
      item = (JSONObject) iter.next();

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
  public void upsert(String indexName, String typeName, JSONObject record) {
    ActionRequestBuilder builder = this.getActionBuilder(indexName, typeName, record);
    builder.get();
  }

  private ActionRequestBuilder getActionBuilder(String indexName, String typeName,
      JSONObject record) {
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
  public <T> T findById(String indexName, String typeName, String id, Class<T> clazz) {
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
  public <T> List<T> findByDSL(String indexName, String typeName, String dsl, Class<T> clazz) {
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
  public <T> List<T> findByDSL(String indexName, String typeName, String dsl, Class<T> clazz,
      String... fields) {

    QueryBuilder query = QueryBuilders.wrapperQuery(dsl);
    SearchRequestBuilder builder = fieldSearchBuilder(indexName, typeName, query,
        fields);
    SearchResponse response = builder.get();

    return parseResponse(response, clazz);
  }

  @Override
  public <T> List<T> findByIds(String indexName, String typeName, Set<String> ids, Class<T> clazz,
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

  @Override
  public <T> ScrolledPageResult<T> scrollSearch(String indexName, String typeName,
      SearchRequest searchRequest, Class<T> clazz) throws IndexNotFoundException {

    QueryBuilder query = buildQuery(searchRequest);

    SearchRequestBuilder builder = client.prepareSearch(indexName)
        .setTypes(typeName)
        .setFetchSource(false)
        .setSize(searchRequest.getSize())
        .setFrom(searchRequest.offset())
        .setQuery(query);

    // parse fields
    if (!CollectionUtils.isEmpty(searchRequest.getFields())) {
      for (String field : searchRequest.getFields()) {
        builder.addDocValueField(field);
      }
    }

    // parse sort
    if (!CollectionUtils.isEmpty(searchRequest.getOrders())) {
      for (Order order : searchRequest.getOrders()) {
        builder.addSort(order.getName(), SortOrder.fromString(order.getOrderType().name()));
      }
    }

    SearchResponse response = builder.get();
    List<T> content = parseResponse(response, clazz);
    ScrolledPageResult result = new ScrolledPageResult();
    result.setContent(content);
    result.setPage(searchRequest.getPage());
    result.setSize(searchRequest.getSize());
    result.setTotal(response.getHits().totalHits);
    return result;
  }

  private QueryBuilder buildQuery(SearchRequest searchRequest) {
    BoolQueryBuilder qb = QueryBuilders.boolQuery();

    // parse keyword
    if (StringUtils.isNotBlank(searchRequest.getKeyword())) {
      qb = qb.must(QueryBuilders.queryStringQuery(searchRequest.getKeyword()));
    }

    // parse filters
    if (!CollectionUtils.isEmpty(searchRequest.getFilters())) {
      for (Filter filter : searchRequest.getFilters()) {
        switch (filter.getFilterType()) {
          case EQ:
            qb = qb.must(QueryBuilders.termQuery(filter.getName(), filter.getValue()));
            break;
          case GT:
            qb = qb.must(QueryBuilders.rangeQuery(filter.getName()).from(filter.getValue()));
            break;
          case LT:
            qb = qb.must(QueryBuilders.rangeQuery(filter.getName()).to(filter.getValue()));
            break;
          default:
            break;
        }
      }
    }

    return qb;
  }

  private <T> List<T> parseResponse(SearchResponse response, Class<T> clazz) {
    List<T> result = Lists.newArrayList();
    for (SearchHit hit : response.getHits().getHits()) {
      JSONObject jo = new JSONObject();
      Set<Entry<String, DocumentField>> entries = hit.getFields().entrySet();
      for (Entry<String, DocumentField> entry : entries) {
        Object value = entry.getValue().getValue();
        if (value instanceof DateTime) {
          DateTime dateTime = (DateTime) value;
          jo.put(entry.getKey(), dateTime.getMillis());
        } else {
          jo.put(entry.getKey(), entry.getValue().getValue());
        }
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