package org.skyline.core.dto;

import java.util.List;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-03-26
 */
@Data
public class SearchRequest {

  private String keyword;

  private String scrollId;

  private List<Filter> filters;

  private List<Order> orders;

  private List<String> fields;

  private int page;

  private int size = 10;

  public int offset() {
    return (page - 1 < 0 ? 0 : page - 1) * size;
  }

}
