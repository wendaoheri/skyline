package org.dayu.core.handler;

/**
 * @author Sean Liu
 * @date 2019-07-09
 */
public interface ApplicationHandler {

  HandlerStatus handle(String key, String content);

  DisplayMessage display();

}