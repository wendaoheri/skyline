package org.skyline.core.handler;

import org.skyline.common.data.ApplicationData;
import org.skyline.common.data.HandlerResult;

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