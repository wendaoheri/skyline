package org.dayu.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.dayu.TestBeanEntry;
import org.dayu.common.model.YarnApplication;
import org.dayu.core.data.ApplicationAnswer;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class JSONMessageSerdesTest {

  @Autowired
  private MessageSerdes messageSerdes;

  @Test
  public void test() {
    YarnApplication app = ApplicationAnswer.getTemplate();
    String message = messageSerdes.serialize(app);
    log.info(message);

    YarnApplication desApp = (YarnApplication) messageSerdes.deserialize(message);
    log.info(desApp.toString());
    assert app.equals(desApp);
  }
}
