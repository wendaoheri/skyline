package org.dayu.common.data;

import java.io.Serializable;

/**
 *
 * Result of ApplicationHandler
 *
 * @author Sean Liu
 * @date 2019-07-29
 */
public interface HandlerResult extends Serializable {

  /**
   * Get the result status of handler This status can be used to recover handler
   *
   * @return Handler status
   */
  HandlerStatus getHandlerStatus();


  /**
   * The value range : [0-100], higher is better,
   *
   * @return score of handler result
   */
  double score();
}
