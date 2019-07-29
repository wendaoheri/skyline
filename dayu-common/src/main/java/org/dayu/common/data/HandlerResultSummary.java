package org.dayu.common.data;

import com.google.common.collect.Lists;
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

  private HandlerStatus handlerStatus;

  @Override
  public DisplayMessage display() {
    return null;
  }

  public void addDetail(HandlerResultDetail detail) {
    if (this.resultDetail == null) {
      this.resultDetail = Lists.newArrayList();
    }
    this.resultDetail.add(detail);
  }

  public void merge(HandlerResultSummary summary) {
    if (summary != null && summary.resultDetail != null) {
      this.resultDetail.addAll(summary.getResultDetail());
    }
  }
}
