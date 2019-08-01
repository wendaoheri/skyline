package org.dayu.core.exception;

/**
 * @author Sean Liu
 * @date 2019-07-13
 */
public class DayuException extends RuntimeException {

  public DayuException() {
    super();
  }

  public DayuException(String message) {
    super(message);
  }
}
