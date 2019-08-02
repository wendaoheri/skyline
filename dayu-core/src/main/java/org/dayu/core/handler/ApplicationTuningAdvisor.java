package org.dayu.core.handler;

import org.dayu.common.data.DisplayMessage;
import org.dayu.common.data.HandlerResult;

/**
 * @author Sean Liu
 * @date 2019-07-09
 */
public interface ApplicationTuningAdvisor extends ApplicationHandler {

  /**
   * return a displayMessage by parse result
   * @param handlerResult handler result
   * @return
   */
  DisplayMessage display(HandlerResult handlerResult);
}
