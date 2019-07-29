package org.dayu.core.handler.dispatcher;

import org.dayu.common.message.Message;

/**
 * @author Sean Liu
 * @date 2019-07-09
 */
public interface MessageDispatcher<IN, OUT> {


  /**
   * dispatch a message to handlers, and return a new message to invoker
   *
   * Another usage of return message can be recovered if any exception occurred
   *
   * @param key message key
   * @param message message content
   * @return A new message
   */
  Message<OUT> dispatch(String key, Message<IN> message);

}
