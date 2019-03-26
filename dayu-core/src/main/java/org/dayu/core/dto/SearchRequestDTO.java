package org.dayu.core.dto;

import java.util.List;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-03-26
 */
@Data
public class SearchRequestDTO {

  private String keyword;

  private List<Filter> filters;

  private List<Order> orders;

}
