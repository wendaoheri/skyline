package org.skyline.core.handler.advisor;

import java.util.SortedMap;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-08-02
 */
@Data
public class AdvisorConfig {

  private String name;
  private String describe;
  private String display;
  private String displayDetail;
  private String measureExp;
  private String weightExp;
  private String scoreExp;
  private double[] limits;
  private int order;
  private SortedMap<String, Object> variables;
}
