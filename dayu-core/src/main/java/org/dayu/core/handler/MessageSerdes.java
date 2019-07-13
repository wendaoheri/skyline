package org.dayu.core.handler;

import org.dayu.common.message.Message;

/**
 * @author Sean Liu
 * @date 2019-07-11
 */
public interface MessageSerdes {

  /**
   * Serialize a message
   * @param message
   * @return
   */
  String serialize(Message message);

  /** Deserialize a object from a bytes
   * @param message
   * @return
   */
  Message deserialize(String message);

}
