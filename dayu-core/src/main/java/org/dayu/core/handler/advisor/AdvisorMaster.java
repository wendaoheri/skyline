package org.dayu.core.handler.advisor;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.HandlerResult;
import org.dayu.common.data.ResultSummary;
import org.dayu.common.model.YarnApplication.ApplicationType;
import org.dayu.core.handler.ApplicationTuningAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
public abstract class AdvisorMaster implements ApplicationContextAware {

  private ApplicationContext context;

  /**
   * Get the type of application that sub-advisor can handle
   *
   * @return type of application
   */
  protected abstract ApplicationType getApplicationType();

  public ResultSummary advise(ApplicationData applicationData) {
    ResultSummary result = new ResultSummary();
    ApplicationType applicationType = this.getApplicationType();
    List<ApplicationTuningAdvisor> advisors = this.getAdvisors(applicationType);

    for (ApplicationTuningAdvisor advisor : advisors) {
      HandlerResult advise = advisor.handle(applicationData);
      result.merge(advise);
    }

    return result;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.context = applicationContext;
  }

  private List<ApplicationTuningAdvisor> getAdvisors(ApplicationType applicationType) {
    Map<String, ApplicationTuningAdvisor> beans = context
        .getBeansOfType(ApplicationTuningAdvisor.class);
    return beans.values().parallelStream()
        .filter(x -> applicationType.equals(x.getApplicationType())).sorted(
            Comparator.comparingInt(ApplicationTuningAdvisor::getOrder))
        .collect(Collectors.toList());
  }

}
