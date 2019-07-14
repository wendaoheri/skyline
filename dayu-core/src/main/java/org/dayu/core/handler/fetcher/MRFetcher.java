package org.dayu.core.handler.fetcher;

import org.dayu.core.handler.ApplicationInfoFetcher;
import org.dayu.core.handler.DisplayMessage;
import org.dayu.core.handler.HandlerStatus;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-13
 */
@Component("mrFetcher")
public class MRFetcher implements ApplicationInfoFetcher {

  @Override
  public HandlerStatus handle(String key, String content) {
    return null;
  }

  @Override
  public DisplayMessage display() {
    return null;
  }
}
