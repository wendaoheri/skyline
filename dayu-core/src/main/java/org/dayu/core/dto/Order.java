package org.dayu.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-03-26
 */
@Data
@AllArgsConstructor
public class Order {

  private OrderType orderType;
  private String name;
  public enum OrderType {
    DESC,
    ASC
  }


}
