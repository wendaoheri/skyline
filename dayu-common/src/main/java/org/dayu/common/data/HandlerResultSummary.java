package org.dayu.common.data;

import java.util.List;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
@Data
public class HandlerResultSummary implements HandlerResult {

  private ApplicationData applicationData;

  private List<HandlerResultDetail> resultDetail;

  private HandlerStatus status;

  @Override
  public DisplayMessage display() {
    return null;
  }
}
