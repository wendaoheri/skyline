package org.dayu.core.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * TODO need refactor, consider MessageType here
 *
 * @author Sean Liu
 * @date 2019-07-11
 */
@Component
@Slf4j
public class JSONMessageSerdes implements MessageSerdes {

  private static final String MESSAGE_CLASS_KEY = "messageClass";
  private static final String MESSAGE_CONTENT_KEY = "messageContent";

  @Override
  public String serialize(Object obj) {

    Class<?> messageClazz = obj.getClass();
    JSONObject jo = new JSONObject();
    jo.put(MESSAGE_CLASS_KEY, messageClazz);
    jo.put(MESSAGE_CONTENT_KEY, obj);

    return jo.toJSONString();
  }

  @Override
  public Object deserialize(String message) {
    JSONObject jo = JSON.parseObject(message);
    Class<?> messageClazz = null;
    try {
      messageClazz = Class.forName(jo.getString(MESSAGE_CLASS_KEY));
    } catch (ClassNotFoundException e) {
      log.error("Cannot final class to deserialize message : {}", jo.getString(MESSAGE_CLASS_KEY));
    }
    if (messageClazz != null) {
      return jo.getObject(MESSAGE_CONTENT_KEY, messageClazz);
    } else {
      return jo.getJSONObject(MESSAGE_CONTENT_KEY);
    }

  }

}
