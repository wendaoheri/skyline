package org.dayu.common.data;

import lombok.Getter;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
public enum Severity {

  /**
   * CRITICAL
   */
  CRITICAL(4),

  /**
   * SEVERE
   */
  SEVERE(3),

  /**
   * MODERATE
   */
  MODERATE(2),

  /**
   * LOW
   */
  LOW(1),

  /**
   * NONE
   */
  NONE(0);

  @Getter
  private int value;

  Severity(int value) {
    this.value = value;
  }

  public static Severity valueOf(int value) {
    for (Severity severity : values()) {
      if (severity.value == value) {
        return severity;
      }
    }
    return NONE;
  }

  public static int length() {
    return values().length;
  }
}
