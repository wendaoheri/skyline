package org.skyline.core.handler.dispatcher;

import lombok.extern.slf4j.Slf4j;
import org.skyline.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-13
 */
@Component("dispatcherMaster")
@Slf4j
public class DispatcherMaster {

  @Autowired
  private FetcherDispatcher fetcherDispatcher;

  @Autowired
  private AdvisorDispatcher advisorDispatcher;

  public Message dispatch(String key, Message message) {
    log.debug("dispatch message {}", message);

    Message returnMessage;
    switch (message.getMessageType()) {
      case APPLICATION_FETCH: {
        log.info("Send message to FetcherDispatcher");
        returnMessage = fetcherDispatcher.dispatch(key, message);
        this.dispatch(key, returnMessage);
        break;
      }
      case APPLICATION_FETCH_DONE: {
        log.info("Send message to AdvisorDispatcher");
        returnMessage = advisorDispatcher.dispatch(key, message);
        this.dispatch(key, returnMessage);
        break;
      }
      default:
        break;
    }
    return null;
  }
}
