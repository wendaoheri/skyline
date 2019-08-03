package org.skyline.core.queue;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyline.common.data.YarnApplication;
import org.skyline.core.TestBeanEntry;
import org.skyline.core.data.ApplicationAnswer;
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
