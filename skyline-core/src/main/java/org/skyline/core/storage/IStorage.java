package org.skyline.core.storage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import org.elasticsearch.index.IndexNotFoundException;
import org.skyline.core.dto.ScrolledPageResult;
import org.skyline.core.dto.SearchRequest;

/**
 * @author Sean Liu
 * @date 2019-04-17
 */
public interface IStorage {

  void trace(List<Object> data);

  void bulkUpsert(String indexName, String typeName, JSONArray records)
      throws ExecutionException, InterruptedException;

  void upsert(String indexName, String typeName, JSONObject record);

  <T> T findById(String indexName, String typeName, String id, Class<T> clazz)
      throws IndexNotFoundException;

  <T> List<T> findByDSL(String indexName, String typeName, String dsl, Class<T> clazz)
      throws IndexNotFoundException;

  <T> List<T> findByDSL(String indexName, String typeName, String dsl, Class<T> clazz,
      String... fields)
      throws IndexNotFoundException;

  <T> ScrolledPageResult<T> scrollSearch(String indexName, String typeName,
      SearchRequest searchRequest, Class<T> clazz)
      throws IndexNotFoundException;

  <T> List<T> findByIds(String indexName, String typeName, Set<String> ids, Class<T> clazz,
      String... fields) throws IndexNotFoundException;

  /**
   * Max size is 10000
   *
   * @param indexName
   * @param typeName
   * @param clazz
   * @param fields
   * @param <T>
   * @return
   * @throws IndexNotFoundException
   */
  <T> List<T> findAll(String indexName, String typeName, Class<T> clazz, boolean fetchSource,String... fields)
      throws IndexNotFoundException;

  boolean indexExists(String indexName);
}
