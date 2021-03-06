package org.skyline.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyline.common.data.YarnApplication;
import org.skyline.common.message.Message;
import org.skyline.common.message.MessageType;
import org.skyline.core.TestBeanEntry;
import org.skyline.core.data.ApplicationAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Sean Liu
 * @date 2019-07-12
 */
@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class JsonMessageSerdesTest {

  @Autowired
  private MessageSerdes messageSerdes;

  @Test
  public void test() {
    YarnApplication app = ApplicationAnswer.getTemplate();
    Message m = new Message();
    m.setMessageType(MessageType.APPLICATION_FETCH);
    m.setMessageContent(app);
    String message = messageSerdes.serialize(m);
    log.info(message);

    Message desM = messageSerdes.deserialize(message);
    log.info(desM.toString());
    YarnApplication desApp = (YarnApplication) desM.getMessageContent();
    assert app.equals(desApp);
  }
}
