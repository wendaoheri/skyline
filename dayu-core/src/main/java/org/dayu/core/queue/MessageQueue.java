package org.dayu.core.queue;

import java.util.Map;

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
   * send multi message to queue with same key
   *
   * @param key message key
   * @param contents content list
   */
  void sendMessage(String key, Iterable<String> contents);


  /**
   * send multi message to queue with different key
   *
   * @param contentWithKey map key is key, map value is content
   */
  void sendMessage(Map<String, String> contentWithKey);

  /**
   * this method should be invoked when a message consumed
   *
   * @param key message key
   * @param content message content
   */
  void handleMessage(String key, String content);

}
