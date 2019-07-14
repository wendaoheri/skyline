package org.dayu.common.data;

import org.dayu.common.model.YarnApplication;

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

}
