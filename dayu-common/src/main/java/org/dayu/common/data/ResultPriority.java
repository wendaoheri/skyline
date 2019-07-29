package org.dayu.common.data;

import lombok.Getter;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
public enum ResultPriority {

  /**
   * major
   */
  MAJOR(1);

  @Getter
  private int priority;

  ResultPriority(int priority) {
    this.priority = priority;
  }
}
