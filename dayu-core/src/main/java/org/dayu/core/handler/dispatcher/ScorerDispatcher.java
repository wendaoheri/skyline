package org.dayu.core.handler.dispatcher;

import org.dayu.common.data.HandlerResult;
import org.dayu.common.message.Message;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-13
 */
@Component
public class ScorerDispatcher implements MessageDispatcher<HandlerResult, HandlerResult> {


  @Override
  public Message<HandlerResult> dispatch(String key, Message<HandlerResult> message) {
    return null;
  }
}
