package org.dayu.core.handler.dispatcher;

import lombok.extern.slf4j.Slf4j;
import org.dayu.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-13
 */
@Component("dispatcherMaster")
@Slf4j
public class DispatcherMaster implements MessageDispatcher {

  @Autowired
  private FetcherDispatcher fetcherDispatcher;

  @Override
  public void dispatch(String key, Message message) {
    log.debug("dispatch message {}", message);

    switch (message.getMessageType()) {
      case APPLICATION_FETCH: {
        log.info("Send message to FetcherDispatcher");
        fetcherDispatcher.dispatch(key, message);
      }
      default:
        break;
    }
  }
}
