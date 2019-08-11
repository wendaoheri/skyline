package org.skyline.core.utils;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
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

    public static final StatSummary ZERO = new StatSummary(0, 0, 0, 0, 0, 0);

    private double mean;
    private int count;
    private double std;
    /**
     * See : https://en.wikipedia.org/wiki/Coefficient_of_variation
     */
    private double cv;
    private double max;
    private double min;
  }

  public static StatSummary summary(long[] values) {
    double[] dValues = new double[values.length];
    for (int i = 0; i < values.length; i++) {
      dValues[i] = values[i];
    }
    return summary(dValues);
  }

  public static StatSummary summary(Collection<Double> values) {
    // TODO handle null Double value
    double[] valueArray = ArrayUtils.toPrimitive(values.toArray(new Double[values.size()]), 0.);
    return summary(valueArray);
  }

  public static StatSummary summary(double[] values) {
    if (values == null || values.length == 0) {
      return StatSummary.ZERO;
    }
    SummaryStatistics stats = new SummaryStatistics();
    for (double value : values) {
      stats.addValue(value);
    }

    StatSummary summary = new StatSummary();
    summary.setCount(values.length);
    summary.setMean(stats.getMean());
    summary.setMax(stats.getMax());
    summary.setMin(stats.getMin());

    //noinspection AlibabaUndefineMagicConstant
    if (summary.count == 1) {
      summary.setStd(0);
      summary.setCv(0);
    } else {
      summary.setStd(stats.getStandardDeviation());
      summary.setCv(summary.std / summary.mean);
    }

    return summary;
  }

}
