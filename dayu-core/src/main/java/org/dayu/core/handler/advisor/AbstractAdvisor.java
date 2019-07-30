package org.dayu.core.handler.advisor;

import java.util.List;
import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.HandlerResult;
import org.dayu.common.data.HandlerResultDetail;
import org.dayu.common.model.YarnApplication.ApplicationType;
import org.dayu.core.exception.HandlerTypeMismatchException;
import org.dayu.core.handler.ApplicationTuningAdvisor;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
public abstract class AbstractAdvisor implements ApplicationTuningAdvisor {

  @Override
  public HandlerResult handle(ApplicationData applicationData) {
    checkApplicationType(applicationData);
    return this.advise(applicationData);
  }

  void checkApplicationType(ApplicationData applicationData) {
    ApplicationType applicationType = applicationData.getApplicationType();
    ApplicationType exceptedApplicationType = getApplicationType();
    if (!applicationType.equals(exceptedApplicationType)) {
      throw new HandlerTypeMismatchException();
    }
  }

  @Override
  public List<HandlerResultDetail> multiAdvise(ApplicationData applicationData) {
    throw new UnsupportedOperationException();
  }
}
