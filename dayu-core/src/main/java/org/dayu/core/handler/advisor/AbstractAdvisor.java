package org.dayu.core.handler.advisor;

import java.util.List;
import lombok.Setter;
import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.HandlerResult;
import org.dayu.common.data.ResultDetail;
import org.dayu.common.model.YarnApplication.ApplicationType;
import org.dayu.core.exception.HandlerTypeMismatchException;
import org.dayu.core.handler.ApplicationTuningAdvisor;
import org.dayu.core.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
public abstract class AbstractAdvisor implements ApplicationTuningAdvisor {

  @Autowired
  private MessageUtils messageUtils;

  @Setter
  private int order;

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
  public List<ResultDetail> multiAdvise(ApplicationData applicationData) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getOrder() {
    return order;
  }

  @Override
  public String describe() {
    return messageUtils.getMessage("describe." + this.getClass().getName());
  }
}
