package org.dayu.core.handler;

/**
 * @author Sean Liu
 * @date 2019-07-11
 */
public interface MessageSerdes {

  /**
   * Serialize a object to bytes
   * @param obj
   * @return
   */
  String serialize(Object obj);

  /** Deserialize a object from a bytes
   * @param message
   * @return
   */
  Object deserialize(String message);

}
