package org.skyline.core.utils;

import java.text.DecimalFormat;

/**
 * @author Sean Liu
 * @date 2019-08-02
 */
public class DisplayUtils {

  static DecimalFormat df = new DecimalFormat("#.##");

  public static String display(double d) {
    return df.format(d);
  }

}
