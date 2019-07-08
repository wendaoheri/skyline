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
public class MessageQueue {

  @Autowired
  private JmsTemplate jmsTemplate;

  public void sendMessage() {
    int message = 0;
    while (true) {
      log.info("sending message : {}", message);
      jmsTemplate.convertAndSend("test_dest","m" + message);
      message++;
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @JmsListener(destination = "test_dest")
  public void processMessage(String content) {
    log.info("got message : {}", content);
  }

}
