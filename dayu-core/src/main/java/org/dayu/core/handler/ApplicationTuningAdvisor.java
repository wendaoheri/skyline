package org.dayu.core.handler;

import java.util.List;
import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.HandlerResultDetail;
import org.dayu.common.model.YarnApplication.ApplicationType;

/**
 * @author Sean Liu
 * @date 2019-07-09
 */
public interface ApplicationTuningAdvisor extends ApplicationHandler {

  /**
   * give one tuning advisement by analysis application
   *
   * @param applicationData application Data
   */
  HandlerResultDetail advise(ApplicationData applicationData);

  /**
   * give multi tuning advisement by analysis application
   *
   * @param applicationData application
   */
  List<HandlerResultDetail> multiAdvise(ApplicationData applicationData);

  /**
   * Get order of advisor list, invoke from small to large
   *
   * @return order of advisor list
   */
  int getOrder();


  /**
   * Application advisor handle same application construct a advise chain. This used for distinguish
   * different advisor
   *
   * @return application type
   */
  ApplicationType getApplicationType();
}
