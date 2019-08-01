package org.dayu.core.utils;

import org.dayu.common.data.Severity;
import org.dayu.core.exception.SeverityLimitMismatchException;

/**
 * Calculate severity
 *
 * @author Sean Liu
 * @date 2019-07-31
 */
public class SeverityUtils {


  public static Severity calSeverity(double value, double[] limits) {

    if (limits.length != Severity.length() - 1) {
      throw new SeverityLimitMismatchException();
    }

    double first = limits[0];
    double last = limits[limits.length - 1];

    boolean ascending = last - first > 0;

    // determine whether limits is ascending or descending,
    // then add -INF and +INF to limits
    double[] realLimits = new double[Severity.length() + 1];
    if (ascending) {
      realLimits[0] = Double.NEGATIVE_INFINITY;
      realLimits[Severity.length()] = Double.POSITIVE_INFINITY;
    } else {
      realLimits[0] = Double.POSITIVE_INFINITY;
      realLimits[Severity.length()] = Double.NEGATIVE_INFINITY;
    }

    for (int i = 0; i < limits.length; i++) {
      realLimits[i + 1] = limits[i];
    }

    for (int i = 0; i < realLimits.length - 1; i++) {
      boolean between =
          (value >= realLimits[i] && value < realLimits[i + 1]) || (value < realLimits[i]
              && value >= realLimits[i + 1]);
      if (between) {
        return Severity.valueOf(i);
      }
    }
    return Severity.NONE;
  }


}
