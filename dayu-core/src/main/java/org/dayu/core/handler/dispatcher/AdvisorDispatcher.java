package org.dayu.core.handler.dispatcher;

import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.HandlerResult;
import org.dayu.common.message.Message;
import org.dayu.common.message.MessageType;
import org.dayu.core.handler.advisor.mr.MRAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-13
 */
@Component
public class AdvisorDispatcher implements
    MessageDispatcher<HandlerResult, HandlerResult> {

  @Autowired
  private MRAdvisor mrAdvisor;

  @Override
  public Message<HandlerResult> dispatch(String key, Message<HandlerResult> message) {

    HandlerResult handlerResult = message.getMessageContent();
    ApplicationData applicationData = handlerResult.getApplicationData();

    HandlerResult advisorResult = null;

    switch (applicationData.getApplicationType()) {
      case MAPREDUCE:
        advisorResult = mrAdvisor.handle(applicationData);
        break;
      default:
        break;
    }
    handlerResult.merge(advisorResult);

    Message<HandlerResult> returnMessage = new Message<>();
    returnMessage.setMessageContent(handlerResult);
    returnMessage.setMessageType(MessageType.APPLICATION_ADVISE_DONE);

    return returnMessage;
  }
}
