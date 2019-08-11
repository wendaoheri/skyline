package org.skyline.core.utils;

import java.beans.FeatureDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author Sean Liu
 * @date 2019-07-07
 */
public class SkylineUtils {

  public static final long DAY_MS = 24 * 3600 * 1000;

  public static <T> T randomChoice(List<T> l) {
    return l.get(RandomUtils.nextInt(0, l.size()));
  }

  public static <T> T randomChoice(T... l) {
    return l[RandomUtils.nextInt(0, l.length)];
  }

  public static <T extends Enum<?>> T randomChoice(Class<T> clazz) {
    int x = RandomUtils.nextInt(0, clazz.getEnumConstants().length);
    return clazz.getEnumConstants()[x];
  }

  public static String randomStr(String prefix, int bound) {
    return prefix + "_" + RandomUtils.nextInt(0, bound);
  }

  public static long randomTimestamp(Long start, Long end) {
    long curr = System.currentTimeMillis();
    if (start == null || start == 0) {
      start = curr - DAY_MS;
    }
    if (end == null || end == 0) {
      end = curr;
    }
    return RandomUtils.nextLong(start, end);
  }

  public static long randomRange(long start, long end) {
    return RandomUtils.nextLong(start, end);
  }

  /**
   * Ensure http url is start with "http://" only once
   */
  public static void checkHttpSchema(String httpUrl) {
    httpUrl = httpUrl.replace("http://", "");
    httpUrl = "http://" + httpUrl;
  }

  public static String readFile(String path) throws IOException {
    InputStream in = SkylineUtils.class.getClassLoader().getResourceAsStream(path);
    return String.join("\n", IOUtils.readLines(in, StandardCharsets.UTF_8));
  }

  public static void copyPropertiesIgnoreNull(Object src, Object target) {
    BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
  }

  public static String[] getNullPropertyNames(Object source) {
    final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
    return Stream.of(wrappedSource.getPropertyDescriptors())
        .map(FeatureDescriptor::getName)
        .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
        .toArray(String[]::new);
  }

}
