package org.dayu.core.exception;

import org.dayu.common.data.Severity;

/**
 * @author Sean Liu
 * @date 2019-07-31
 */
public class SeverityLimitMismatchException extends DayuException {

  public SeverityLimitMismatchException() {
    super("Severity limit length must be " + (Severity.values().length - 1));
  }

}
