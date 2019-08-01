package org.dayu.common.data;

import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
@Data
public class ResultDetail implements HandlerResult {


  private Severity severity;

  private String handlerName;

  private HandlerStatus handlerStatus;


  @Override
  public HandlerStatus getHandlerStatus() {
    return handlerStatus;
  }

  @Override
  public double score() {
    return 0;
  }

}
