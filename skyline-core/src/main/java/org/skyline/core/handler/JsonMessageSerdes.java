package org.skyline.core.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.skyline.common.message.Message;
import org.skyline.common.message.MessageType;
import org.springframework.stereotype.Component;

/**
 * TODO need refactor, consider MessageType here
 *
 * @author Sean Liu
 * @date 2019-07-11
 */
@Component
@Slf4j
public class JsonMessageSerdes implements MessageSerdes {

  private static final String MESSAGE_CLASS_KEY = "messageClass";
  private static final String MESSAGE_CONTENT_KEY = "messageContent";
  private static final String MESSAGE_TYPE_KEY = "messageType";

  @Override
  public String serialize(Message message) {
    Object obj = message.getMessageContent();
    MessageType messageType = message.getMessageType();
    Class<?> messageClazz = obj.getClass();
    JSONObject jo = new JSONObject();
    jo.put(MESSAGE_CLASS_KEY, messageClazz);
    jo.put(MESSAGE_CONTENT_KEY, obj);
    jo.put(MESSAGE_TYPE_KEY, messageType);
    return jo.toJSONString();
  }

  @Override
  public Message deserialize(String message) {
    Message result = new Message();
    JSONObject jo = JSON.parseObject(message);
    Class<?> messageClazz = null;
    try {
      messageClazz = Class.forName(jo.getString(MESSAGE_CLASS_KEY));
    } catch (ClassNotFoundException e) {
      log.error("Cannot final class to deserialize message : {}", jo.getString(MESSAGE_CLASS_KEY));
    }
    MessageType messageType = jo.getObject(MESSAGE_TYPE_KEY, MessageType.class);

    Object messageContent = null;
    if (messageClazz != null) {
      messageContent = jo.getObject(MESSAGE_CONTENT_KEY, messageClazz);
    } else {
      messageContent = jo.getJSONObject(MESSAGE_CONTENT_KEY);
    }
    result.setMessageContent(messageContent);
    result.setMessageType(messageType);
    return result;
  }

}
