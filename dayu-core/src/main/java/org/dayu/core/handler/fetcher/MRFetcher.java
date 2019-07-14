package org.dayu.core.handler.fetcher;

import lombok.extern.slf4j.Slf4j;
import org.dayu.common.data.ApplicationData;
import org.dayu.core.handler.ApplicationInfoFetcher;
import org.dayu.core.handler.DisplayMessage;
import org.dayu.core.handler.HandlerStatus;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-13
 */
@Component("mrFetcher")
@Slf4j
public class MRFetcher implements ApplicationInfoFetcher {

  @Override
  public HandlerStatus handle(ApplicationData applicationData) {
    log.info("got Application data : {}", applicationData);
    return null;
  }

  @Override
  public DisplayMessage display() {
    return null;
  }
}
