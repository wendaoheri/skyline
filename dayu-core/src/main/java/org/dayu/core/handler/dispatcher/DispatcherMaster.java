package org.dayu.core.handler.dispatcher;

import lombok.extern.slf4j.Slf4j;
import org.dayu.common.message.Message;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-13
 */
@Component("dispatcherMaster")
@Slf4j
public class DispatcherMaster implements MessageDispatcher {

  @Override
  public void dispatch(String key, Message message) {
    log.info("dispatch message {}", message);
  }
}
