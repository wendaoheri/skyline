package org.skyline.core.queue;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.skyline.common.message.Message;
import org.skyline.core.handler.MessageSerdes;
import org.skyline.core.handler.dispatcher.DispatcherMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-09
 */
@Component
@Slf4j
public class ActiveMessageQueue implements MessageQueue {

  private static final String DEFAULT_DEST_NAME = "default";
  @Autowired
  private JmsTemplate jmsTemplate;
  @Autowired
  private DispatcherMaster dispatcher;
  @Autowired
  private MessageSerdes messageSerdes;

  @Override
  public void sendMessage(String key, String content) {
    jmsTemplate.convertAndSend(DEFAULT_DEST_NAME, content);
  }

  @Override
  public void sendMessage(String key, Iterable<String> contents) {
    contents.forEach(c -> sendMessage(key, c));
  }

  @Override
  public void sendMessage(Map<String, String> contentWithKey) {
    contentWithKey.forEach((k, v) -> sendMessage(k, v));
  }

  @Override
  public void handleMessage(String key, String content) {
    Message message = messageSerdes.deserialize(content);
    dispatcher.dispatch(key, message);
  }


  @JmsListener(destination = DEFAULT_DEST_NAME)
  public void processMessage(String content) {
    handleMessage(null, content);
    log.debug("got message : {}", content);
  }
}
