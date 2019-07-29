package org.dayu.core.handler.dispatcher;

import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.HandlerResultSummary;
import org.dayu.common.message.Message;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-13
 */
@Component
public class AdvisorDispatcher implements
    MessageDispatcher<HandlerResultSummary, HandlerResultSummary> {


  @Override
  public Message<HandlerResultSummary> dispatch(String key, Message<HandlerResultSummary> message) {
    HandlerResultSummary resultSummary = message.getMessageContent();
    ApplicationData applicationData = resultSummary.getApplicationData();
    return null;
  }
}
