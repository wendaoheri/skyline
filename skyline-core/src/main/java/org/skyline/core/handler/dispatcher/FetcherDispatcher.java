package org.skyline.core.handler.dispatcher;

import lombok.extern.slf4j.Slf4j;
import org.skyline.common.data.ApplicationData;
import org.skyline.common.data.HandlerResult;
import org.skyline.common.data.YarnApplication;
import org.skyline.common.data.YarnApplication.ApplicationType;
import org.skyline.common.data.mr.MRApplicationData;
import org.skyline.common.data.tez.TezApplicationData;
import org.skyline.common.message.Message;
import org.skyline.common.message.MessageType;
import org.skyline.core.handler.fetcher.MRFetcher;
import org.skyline.core.handler.fetcher.SparkFetcher;
import org.skyline.core.handler.fetcher.TezFetcher;
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
    Message<HandlerResult> returnMessage = new Message<>();
    YarnApplication application = message.getMessageContent();
    log.debug("dispatch application : {}", application);

    if (!application.isFinished()) {
      returnMessage.setMessageType(MessageType.APPLICATION_FETCH_SKIPPED);
      return returnMessage;
    }

    ApplicationData data = newApplicationDataByType(
        ApplicationType.valueOf(application.getApplicationType()));
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

    returnMessage.setMessageContent(result);
    returnMessage.setMessageType(MessageType.APPLICATION_FETCH_DONE);
    return returnMessage;
  }

  ApplicationData newApplicationDataByType(ApplicationType type) {
    switch (type) {
      case MAPREDUCE:
        return new MRApplicationData();
      case TEZ:
        return new TezApplicationData();
      default:
        return null;
    }
  }

}
