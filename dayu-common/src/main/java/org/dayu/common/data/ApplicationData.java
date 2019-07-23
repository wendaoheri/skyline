package org.dayu.common.data;

import java.util.Properties;
import org.dayu.common.model.YarnApplication;
import org.dayu.common.model.YarnApplication.ApplicationType;

/**
 * interface produced by ApplicationHandler
 *
 * @author Sean Liu
 * @date 2019-07-14
 */
public interface ApplicationData {

  /**
   * set YarnApplication of handlable data
   * @param application yarn application
   */
  void setApplication(YarnApplication application);

  /**
   * return applicationId
   *
   * @return application Id
   */
  String getApplicationId();

  /**
   * return application type
   *
   * @return applicationType
   */
  ApplicationType getApplicationType();

  /**
   * return application config
   *
   * @return config of application
   */
  Properties getConf();
}
