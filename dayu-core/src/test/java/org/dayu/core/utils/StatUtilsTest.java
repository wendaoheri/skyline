package org.dayu.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.dayu.core.utils.StatUtils.StatSummary;
import org.junit.Test;

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
