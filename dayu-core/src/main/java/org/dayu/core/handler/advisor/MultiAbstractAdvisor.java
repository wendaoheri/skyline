package org.dayu.core.handler.advisor;

import java.util.List;
import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.HandlerResult;
import org.dayu.common.data.ResultDetail;
import org.dayu.common.data.ResultSummary;

/**
 * @author Sean Liu
 * @date 2019-07-30
 */
public abstract class MultiAbstractAdvisor extends AbstractAdvisor {

  @Override
  public HandlerResult handle(ApplicationData applicationData) {
    ResultSummary summary = new ResultSummary();
    checkApplicationType(applicationData);
    List<ResultDetail> results = this.multiAdvise(applicationData);
    summary.addDetail(results);
    return summary;
  }

  @Override
  public final ResultDetail advise(ApplicationData applicationData) {
    throw new UnsupportedOperationException();
  }


}
