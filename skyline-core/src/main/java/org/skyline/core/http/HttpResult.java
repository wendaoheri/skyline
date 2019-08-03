package org.skyline.core.http;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * HttpResult
 *
 * @author Sean
 * @date 2019/2/4
 */
@Data
@AllArgsConstructor
public class HttpResult {

  /**
   * 响应码
   */
  private Integer code;

  /**
   * 响应体
   */
  private String body;
}
