package org.skyline.core.exception;

import org.skyline.common.data.Severity;

/**
 * @author Sean Liu
 * @date 2019-07-31
 */
public class SeverityLimitMismatchException extends SkylineException {

  public SeverityLimitMismatchException() {
    super("Severity limit length must be " + (Severity.values().length - 1));
  }

}
