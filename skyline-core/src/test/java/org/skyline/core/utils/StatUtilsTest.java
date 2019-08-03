package org.skyline.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.skyline.core.utils.StatUtils.StatSummary;

/**
 * @author Sean Liu
 * @date 2019-07-30
 */
@Slf4j
public class StatUtilsTest {

  @Test
  public void testSummary() {
    double[] values = {1.};
    StatSummary summary = StatUtils.summary(values);
    log.info("summary : {}", summary);
  }

}
