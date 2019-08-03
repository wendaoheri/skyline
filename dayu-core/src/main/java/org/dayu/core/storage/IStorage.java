package org.dayu.core.storage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import org.elasticsearch.index.IndexNotFoundException;

/**
 * @author Sean Liu
 * @date 2019-04-17
 */
public interface IStorage {

  void trace(List<Object> data);

  void bulkUpsert(String indexName, String typeName, JSONArray records)
      throws ExecutionException, InterruptedException;

  void upsert(String indexName, String typeName, JSONObject record);

  <T> T findById(String indexName, String typeName, String id, Type clazz)
      throws IndexNotFoundException;

  <T> List<T> findByDSL(String indexName, String typeName, String dsl, Type clazz)
      throws IndexNotFoundException;

  <T> List<T> findByDSL(String indexName, String typeName, String dsl, Type clazz, String... fields)
      throws IndexNotFoundException;

  <T> List<T> findByIds(String indexName, String typeName, Set<String> ids, Type clazz,
      String... fields) throws IndexNotFoundException;
}
