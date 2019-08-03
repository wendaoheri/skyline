package org.skyline.core.handler.fetcher;

import org.skyline.common.data.ApplicationData;
import org.skyline.common.data.HandlerResult;
import org.skyline.core.handler.ApplicationInfoFetcher;
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
