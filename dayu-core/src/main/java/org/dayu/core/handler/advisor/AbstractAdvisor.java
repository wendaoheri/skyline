package org.dayu.core.handler.advisor;

import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.HandlerResult;
import org.dayu.core.handler.ApplicationTunningAdvisor;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
public abstract class AbstractAdvisor implements ApplicationTunningAdvisor {

  private AbstractAdvisor nextAdvisor;

  @Override
  public final HandlerResult handle(ApplicationData applicationData) {
    return this.advise(applicationData);
  }

  public void setNextAdvisor(AbstractAdvisor advisor) {
    this.nextAdvisor = advisor;
  }
}
