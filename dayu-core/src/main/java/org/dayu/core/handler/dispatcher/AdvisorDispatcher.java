package org.dayu.core.handler.dispatcher;

import org.dayu.common.message.Message;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-13
 */
@Component
public class AdvisorDispatcher implements MessageDispatcher {

  @Override
  public void dispatch(String key, Message message) {

  }
}
