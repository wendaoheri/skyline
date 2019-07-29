package org.dayu.core.handler;

import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.HandlerResultDetail;

/**
 * @author Sean Liu
 * @date 2019-07-09
 */
public interface ApplicationTunningAdvisor extends ApplicationHandler {

  HandlerResultDetail advise(ApplicationData applicationData);

}
