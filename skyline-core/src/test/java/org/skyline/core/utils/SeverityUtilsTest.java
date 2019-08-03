package org.skyline.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.skyline.common.data.Severity;

/**
 * @author Sean Liu
 * @date 2019-07-31
 */
@Slf4j
public class SeverityUtilsTest {

  @Test
  public void testCalSeverity() {
    double value = 0.3;
    double[] limits = {4, 3, 2, 1};
    Severity severity = SeverityUtils.calSeverity(value, limits);
    log.info("Severity : {}", severity);
  }

}
