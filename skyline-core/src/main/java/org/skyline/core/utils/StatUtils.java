package org.skyline.core.utils;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 * Statistics tools
 *
 * @author Sean Liu
 * @date 2019-07-30
 */
public class StatUtils {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class StatSummary {

    public static final StatSummary ZERO = newZero();

    private double mean;
    private int count;
    private double std;
    /**
     * See : https://en.wikipedia.org/wiki/Coefficient_of_variation
     */
    private double cv;
    private double max;
    private double min;

    static StatSummary newZero() {
      return new StatSummary(0, 0, 0, 0, 0, 0);
    }
  }

  public static StatSummary summary(Collection<? extends Number> values) {
    Number[] valueArr = new Number[values.size()];
    return summary(values.toArray(valueArr));
  }

  public static StatSummary summary(Number[] values) {
    if (values == null || values.length == 0) {
      return StatSummary.ZERO;
    }
    SummaryStatistics stats = new SummaryStatistics();
    for (Number value : values) {
      if (value != null) {
        stats.addValue(value.doubleValue());
      }
    }
    if (stats.getN() == 0) {
      StatSummary zero = StatSummary.newZero();
      zero.setCount(values.length);
      return zero;
    }
    StatSummary summary = new StatSummary();
    summary.setCount(values.length);
    summary.setMean(stats.getMean());
    summary.setMax(stats.getMax());
    summary.setMin(stats.getMin());

    //noinspection AlibabaUndefineMagicConstant
    if (stats.getN() == 1) {
      summary.setStd(0);
      summary.setCv(0);
    } else {
      summary.setStd(stats.getStandardDeviation());
      summary.setCv(summary.std / summary.mean);
    }

    return summary;
  }

}
