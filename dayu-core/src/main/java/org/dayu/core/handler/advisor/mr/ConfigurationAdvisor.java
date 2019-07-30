package org.dayu.core.handler.advisor.mr;

import lombok.Setter;
import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.HandlerResultDetail;
import org.dayu.common.model.YarnApplication.ApplicationType;
import org.dayu.core.handler.advisor.AbstractAdvisor;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
@Component
public class ConfigurationAdvisor extends AbstractAdvisor {

  @Setter
  private int order;

  @Override
  public HandlerResultDetail advise(ApplicationData applicationData) {
    return null;
  }

  @Override
  public int getOrder() {
    return order;
  }

  @Override
  public ApplicationType getApplicationType() {
    return ApplicationType.MAPREDUCE;
  }

}
