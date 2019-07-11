package org.dayu.core.handler;

/**
 * @author Sean Liu
 * @date 2019-07-09
 */
public interface MessageHandler {

  void handle(String key, String content);

}
