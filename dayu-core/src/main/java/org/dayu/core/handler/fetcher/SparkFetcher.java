package org.dayu.core.handler.fetcher;

import org.dayu.common.data.ApplicationData;
import org.dayu.core.handler.ApplicationInfoFetcher;
import org.dayu.core.handler.DisplayMessage;
import org.dayu.core.handler.HandlerStatus;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-13
 */
@Component("sparkFetcher")
public class SparkFetcher implements ApplicationInfoFetcher {

  @Override
  public HandlerStatus handle(ApplicationData applicationData) {
    return null;
  }

  @Override
  public DisplayMessage display() {
    return null;
  }
}
