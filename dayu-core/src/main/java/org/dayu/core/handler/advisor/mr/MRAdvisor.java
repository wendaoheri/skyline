package org.dayu.core.handler.advisor.mr;

import org.dayu.common.model.YarnApplication.ApplicationType;
import org.dayu.core.handler.advisor.AdvisorMaster;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
@Component
public class MRAdvisor extends AdvisorMaster {

  @Override
  protected ApplicationType getApplicationType() {
    return ApplicationType.MAPREDUCE;
  }
}