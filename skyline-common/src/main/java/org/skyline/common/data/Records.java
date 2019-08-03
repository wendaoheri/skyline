package org.skyline.common.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sean Liu
 * @date 2019-04-17
 */
@Slf4j
public class Records {

  public static JSONArray fromObject(Collection col) {
    return (JSONArray) JSON.toJSON(col);
  }

  public static JSONObject fromObject(Object o) {
    return (JSONObject) JSON.toJSON(o);
  }
}
