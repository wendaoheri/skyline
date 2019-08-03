package org.skyline.common.data;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
@Data
public class HandlerResult {

  private ApplicationData applicationData;

  private List<AdviseDetail> adviseDetails;

  private HandlerStatus handlerStatus;

  public void addDetail(AdviseDetail detail) {
    if (this.adviseDetails == null) {
      this.adviseDetails = Lists.newArrayList();
    }
    this.adviseDetails.add(detail);
  }

  public void addDetail(List<AdviseDetail> details) {
    for (AdviseDetail detail : details) {
      this.addDetail(detail);
    }
  }

  /**
   * merge a summary or detail
   *
   * if result is summary, then add summary's details to summary, else invoke addDetail method
   */
  public void merge(HandlerResult result) {
    if (result != null && result.adviseDetails != null) {
      this.addDetail(result.adviseDetails);
    }
  }

}
