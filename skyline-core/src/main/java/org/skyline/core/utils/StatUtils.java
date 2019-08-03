package org.skyline.core.utils;

import java.util.Collection;
import lombok.Data;
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
  public static class StatSummary {

    private double mean;
    private int count;
    private double std;
    /**
     * See : https://en.wikipedia.org/wiki/Coefficient_of_variation
     */
    private double cv;
  }

  public static StatSummary summary(long[] values) {
    double[] dValues = new double[values.length];
    for (int i = 0; i < values.length; i++) {
      dValues[i] = values[i];
    }
    return summary(dValues);
  }

  public static StatSummary summary(Collection<Double> values) {
    double[] valueArray = ArrayUtils.toPrimitive(values.toArray(new Double[values.size()]), 0.);
    return summary(valueArray);
  }

  public static StatSummary summary(double[] values) {
    SummaryStatistics stats = new SummaryStatistics();
    for (double value : values) {
      stats.addValue(value);
    }

    StatSummary summary = new StatSummary();
    summary.setCount(values.length);
    summary.setMean(stats.getMean());
    summary.setStd(stats.getStandardDeviation());
    summary.setCv(summary.std / summary.mean);

    return summary;
  }

}
