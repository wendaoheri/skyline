package org.skyline.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-03-26
 */

@Data
@AllArgsConstructor
public class Filter {

  private FilterType filterType;
  private String name;
  private Object value;

  public enum FilterType {
    EQ,
    GT,
    LT
  }

}
