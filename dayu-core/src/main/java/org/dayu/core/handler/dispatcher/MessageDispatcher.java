package org.dayu.core.handler.dispatcher;

import org.dayu.common.message.Message;

/**
 * @author Sean Liu
 * @date 2019-07-09
 */
public interface MessageDispatcher {


  /**
   * dispatch a message to handlers
   *
   * @param key message key
   * @param message message content
   */
  void dispatch(String key, Message message);

}
