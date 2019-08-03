package org.skyline.core.handler.dispatcher;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyline.common.data.HandlerResult;
import org.skyline.common.message.Message;
import org.skyline.common.message.MessageType;
import org.skyline.core.TestBeanEntry;
import org.skyline.core.handler.advisor.mr.BaseAdvisorTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Sean Liu
 * @date 2019-08-01
 */
@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class AdvisorDispatcherTest extends BaseAdvisorTest {

  @Autowired
  private AdvisorDispatcher dispatcher;

  @Test
  public void testDispatchMR() {
    Message<HandlerResult> message = new Message<>();
    HandlerResult handlerResult = new HandlerResult();
    handlerResult.setApplicationData(super.generateApplicationData());
    message.setMessageContent(handlerResult);
    message.setMessageType(MessageType.APPLICATION_FETCH_DONE);

    Message<HandlerResult> result = dispatcher.dispatch(null, message);

    log.info("Dispatch result : {}", JSON.toJSONString(result, true));
  }
}
