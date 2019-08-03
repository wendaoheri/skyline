package org.dayu.core.handler.dispatcher;

import lombok.extern.slf4j.Slf4j;
import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.HandlerResult;
import org.dayu.common.data.YarnApplication;
import org.dayu.common.data.YarnApplication.ApplicationType;
import org.dayu.common.data.mr.MRApplicationData;
import org.dayu.common.message.Message;
import org.dayu.common.message.MessageType;
import org.dayu.core.handler.fetcher.MRFetcher;
import org.dayu.core.handler.fetcher.SparkFetcher;
import org.dayu.core.handler.fetcher.TezFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-13
 */
@Component("fetcherDispatcher")
@Slf4j
public class FetcherDispatcher implements MessageDispatcher<YarnApplication, HandlerResult> {

  @Autowired
  private MRFetcher mrFetcher;

  @Autowired
  private SparkFetcher sparkFetcher;

  @Autowired
  private TezFetcher tezFetcher;

  @Override
  public Message<HandlerResult> dispatch(String key, Message<YarnApplication> message) {

    YarnApplication application = message.getMessageContent();
    log.debug("dispatch application : {}", application);

    ApplicationData data = new MRApplicationData();
    data.setApplication(application);
    HandlerResult result = null;
    switch (ApplicationType.valueOf(application.getApplicationType())) {
      case MAPREDUCE:
        result = mrFetcher.handle(data);
        break;
      case TEZ:
        result = tezFetcher.handle(data);
        break;
      case SPARK:
        result = sparkFetcher.handle(data);
        break;
      default:
        break;
    }

    Message<HandlerResult> returnMessage = new Message<>();
    returnMessage.setMessageContent(result);
    returnMessage.setMessageType(MessageType.APPLICATION_FETCH_DONE);
    return returnMessage;
  }

}
