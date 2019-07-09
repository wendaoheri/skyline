package org.dayu.core.queue;

import lombok.extern.slf4j.Slf4j;
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

  @Autowired
  private JmsTemplate jmsTemplate;

  @Override
  public void sendMessage(String key, String content) {
    int message = 0;
    while (true) {
      log.info("sending message : {}", message);
      jmsTemplate.convertAndSend("test_dest", "m" + message);
      message++;
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void handleMessage(String key, String content) {

  }


  @JmsListener(destination = "test_dest")
  public void processMessage(String content) {
    handleMessage(null, content);
    log.info("got message : {}", content);
  }
}
