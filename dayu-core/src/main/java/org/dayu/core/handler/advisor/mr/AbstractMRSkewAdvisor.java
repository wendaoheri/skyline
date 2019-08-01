package org.dayu.core.handler.advisor.mr;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.ResultDetail;
import org.dayu.common.data.Severity;
import org.dayu.common.model.YarnApplication.ApplicationType;
import org.dayu.core.handler.advisor.AbstractAdvisor;
import org.dayu.core.utils.SeverityUtils;
import org.dayu.core.utils.StatUtils;
import org.dayu.core.utils.StatUtils.StatSummary;

/**
 * This advisor calculate skewness of map or reduce tasks, include - execute time - file bytes read
 * - file bytes write
 *
 * @author Sean Liu
 * @date 2019-07-30
 */
@Slf4j
public abstract class AbstractMRSkewAdvisor extends AbstractAdvisor {

  @Setter
  private double[] limits;

  /**
   * Get data list need analysis
   *
   * @param applicationData data
   * @return metric data
   */
  protected abstract long[] getData(ApplicationData applicationData);

  @Override
  public ResultDetail advise(ApplicationData applicationData) {
    ResultDetail detail = new ResultDetail();
    detail.setHandlerName(this.getClass().getName());

    long[] data = getData(applicationData);
    StatSummary summary = StatUtils.summary(data);
    log.info("summary : {}", summary);
    Severity severity = SeverityUtils.calSeverity(summary.getCv(), limits);
    detail.setSeverity(severity);
    return detail;
  }


  @Override
  public ApplicationType getApplicationType() {
    return ApplicationType.MAPREDUCE;
  }
}
