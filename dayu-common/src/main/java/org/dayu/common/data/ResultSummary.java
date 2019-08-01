package org.dayu.common.data;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
@Data
public class ResultSummary implements HandlerResult {

  private ApplicationData applicationData;

  private List<ResultDetail> resultDetail;

  private HandlerStatus handlerStatus;

  public void addDetail(ResultDetail detail) {
    if (this.resultDetail == null) {
      this.resultDetail = Lists.newArrayList();
    }
    this.resultDetail.add(detail);
  }

  public void addDetail(List<ResultDetail> details) {
    for (ResultDetail detail : details) {
      this.addDetail(detail);
    }
  }

  /**
   * merge a summary or detail
   *
   * if result is summary, then add summary's details to summary, else invoke addDetail method
   */
  public void merge(HandlerResult result) {
    if (result instanceof ResultSummary) {
      ResultSummary summary = (ResultSummary) result;
      if (summary != null && summary.resultDetail != null) {
        this.addDetail(summary.resultDetail);
      }
    } else {
      ResultDetail detail = (ResultDetail) result;
      this.addDetail(detail);
    }
  }

  @Override
  public double score() {
    return 0;
  }
}
