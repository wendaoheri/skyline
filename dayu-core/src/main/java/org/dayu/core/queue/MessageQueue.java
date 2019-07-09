package org.dayu.core.queue;

/**
 * @author Sean Liu
 * @date 2019-07-09
 */

public interface MessageQueue {

  /**
   * send message to MQ
   *
   * @param key message key
   * @param content message content
   */
  void sendMessage(String key, String content);

  /**
   * this method should be invoked when a message consumed
   *
   * @param key message key
   * @param content message content
   */
  void handleMessage(String key, String content);

}
