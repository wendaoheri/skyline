package org.dayu.core.handler;

import org.dayu.common.data.ApplicationData;

/**
 * @author Sean Liu
 * @date 2019-07-09
 */
public interface ApplicationHandler {

  HandlerStatus handle(ApplicationData applicationData);

  DisplayMessage display();

}