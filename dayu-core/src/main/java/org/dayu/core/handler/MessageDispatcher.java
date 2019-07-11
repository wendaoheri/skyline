package org.dayu.core.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-09
 */
@Component
public class MessageDispatcher {

  @Autowired
  private MessageSerdes serdes;

  /**
   * dispatch a message to handlers
   *
   * @param key message key
   * @param content message content
   */
  public void dispatch(String key, String content) {

  }

}
