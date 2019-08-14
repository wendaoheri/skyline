package org.skyline.core.utils;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
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
    List<Long> values = Lists.newArrayList(1L, 2L, null);
    StatSummary summary = StatUtils.summary(values);
    log.info("summary : {}", summary);
  }

}
