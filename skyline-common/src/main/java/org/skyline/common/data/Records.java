package org.skyline.common.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sean Liu
 * @date 2019-04-17
 */
@Slf4j
public class Records {

  public static JSONArray fromObject(Collection col) {
    JSONArray ja = (JSONArray) JSON.toJSON(col);
    for (int i = 0; i < ja.size(); i++) {
      JSONObject jo = ja.getObject(i, JSONObject.class);
      Iterables.removeIf(jo.values(), Predicates.isNull());
    }
    return ja;
  }

  public static JSONObject fromObject(Object o) {
    JSONObject jo = (JSONObject) JSON.toJSON(o);
    Iterables.removeIf(jo.values(), Predicates.isNull());
    return jo;
  }

}
