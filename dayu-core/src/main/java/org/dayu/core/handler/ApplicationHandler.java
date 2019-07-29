package org.dayu.core.handler;

import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.HandlerResult;

/**
 * @author Sean Liu
 * @date 2019-07-09
 */
public interface ApplicationHandler {

  /**
   * handle application data
   */
  HandlerResult handle(ApplicationData applicationData);


}