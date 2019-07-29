package org.dayu.core.handler.fetcher;

import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.HandlerResult;
import org.dayu.core.handler.ApplicationInfoFetcher;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-13
 */
@Component("sparkFetcher")
public class SparkFetcher implements ApplicationInfoFetcher {

  @Override
  public HandlerResult handle(ApplicationData applicationData) {
    return null;
  }
}
