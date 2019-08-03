package org.dayu.core.queue;

import lombok.extern.slf4j.Slf4j;
import org.dayu.TestBeanEntry;
import org.dayu.common.data.YarnApplication;
import org.dayu.core.data.ApplicationAnswer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Sean Liu
 * @date 2019-07-09
 */
@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class MessageQueueTest {

  @Autowired
  private MessageQueue mq;


  @Test
  public void test() throws InterruptedException {
    YarnApplication app = ApplicationAnswer.getTemplate();
    mq.sendMessage("", app.toString());
  }

}
