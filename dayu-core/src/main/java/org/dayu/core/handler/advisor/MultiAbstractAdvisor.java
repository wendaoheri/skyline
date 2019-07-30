package org.dayu.core.handler.advisor;

import java.util.List;
import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.HandlerResult;
import org.dayu.common.data.HandlerResultDetail;
import org.dayu.common.data.HandlerResultSummary;

/**
 * @author Sean Liu
 * @date 2019-07-30
 */
public abstract class MultiAbstractAdvisor extends AbstractAdvisor {

  @Override
  public HandlerResult handle(ApplicationData applicationData) {
    HandlerResultSummary summary = new HandlerResultSummary();
    checkApplicationType(applicationData);
    List<HandlerResultDetail> results = this.multiAdvise(applicationData);
    summary.addDetail(results);
    return summary;
  }

  @Override
  public final HandlerResultDetail advise(ApplicationData applicationData) {
    throw new UnsupportedOperationException();
  }


}
