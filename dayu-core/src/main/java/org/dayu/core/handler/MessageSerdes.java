package org.dayu.core.handler;

import org.dayu.common.message.Message;

/**
 * @author Sean Liu
 * @date 2019-07-11
 */
public interface MessageSerdes {

  /**
   * Serialize a message
   */
  String serialize(Message message);

  /**
   * Deserialize a object from a bytes
   */
  Message deserialize(String message);

}
