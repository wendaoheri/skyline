package org.dayu.core.handler.dispatcher;

import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.ResultSummary;
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
    MessageDispatcher<ResultSummary, ResultSummary> {

  @Autowired
  private MRAdvisor mrAdvisor;

  @Override
  public Message<ResultSummary> dispatch(String key, Message<ResultSummary> message) {

    ResultSummary resultSummary = message.getMessageContent();
    ApplicationData applicationData = resultSummary.getApplicationData();

    ResultSummary advisorSummary = null;

    switch (applicationData.getApplicationType()) {
      case MAPREDUCE:
        advisorSummary = mrAdvisor.advise(applicationData);
        break;
      default:
        break;
    }
    resultSummary.merge(advisorSummary);

    Message<ResultSummary> returnMessage = new Message<>();
    returnMessage.setMessageContent(resultSummary);
    returnMessage.setMessageType(MessageType.APPLICATION_ADVISE_DONE);

    return returnMessage;
  }
}
