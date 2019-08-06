package org.skyline.core.utils;

/**
 * @author Sean Liu
 * @date 2019-08-06
 */
public class MemoryUtils {

  public enum MemoryUnit {
    /**
     * KB
     */
    KB(1024),
    /**
     * MB
     */
    MB(1024 * 1024),
    /**
     * GB
     */
    GB(1024 * 1024 * 1024);

    public long value;

    MemoryUnit(long value) {
      this.value = value;
    }

  }

  public static long mbToBytes(long mb) {
    return mb * MemoryUnit.MB.value;
  }

}
