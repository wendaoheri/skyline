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

  public void addDetail(List<HandlerResultDetail> details) {
    for (HandlerResultDetail detail : details) {
      this.addDetail(detail);
    }
  }

  /**
   * merge a summary or detail
   *
   * if result is summary, then add summary's details to summary, else invoke addDetail method
   */
  public void merge(HandlerResult result) {
    if (result instanceof HandlerResultSummary) {
      HandlerResultSummary summary = (HandlerResultSummary) result;
      if (summary != null && summary.resultDetail != null) {
        this.addDetail(summary.resultDetail);
      }
    } else {
      HandlerResultDetail detail = (HandlerResultDetail) result;
      this.addDetail(detail);
    }
  }
}
