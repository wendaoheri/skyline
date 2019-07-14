package org.dayu.core.handler.dispatcher;

import lombok.extern.slf4j.Slf4j;
import org.dayu.common.message.Message;
import org.dayu.common.model.YarnApplication;
import org.dayu.core.handler.fetcher.MRFetcher;
import org.dayu.core.handler.fetcher.SparkFetcher;
import org.dayu.core.handler.fetcher.TezFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-13
 */
@Component("fetcherDispatcher")
@Slf4j
public class FetcherDispatcher implements MessageDispatcher {

  @Autowired
  private MRFetcher mrFetcher;

  @Autowired
  private SparkFetcher sparkFetcher;

  @Autowired
  private TezFetcher tezFetcher;

  @Override
  public void dispatch(String key, Message message) {
    Object messageContent = message.getMessageContent();
    if (messageContent instanceof YarnApplication) {
      YarnApplication application = (YarnApplication) messageContent;
      log.info("dispatch application : {}", application);
    }
  }
}
