package org.dayu.common.data;

import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
@Data
public class HandlerResultDetail implements HandlerResult {

  private DisplayMessage displayMessage;

  private ResultPriority priority;

  @Override
  public DisplayMessage display() {
    return null;
  }

  @Override
  public HandlerStatus getHandlerStatus() {
    return null;
  }

}