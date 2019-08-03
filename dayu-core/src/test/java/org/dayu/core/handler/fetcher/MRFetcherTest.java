package org.dayu.core.handler.fetcher;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.dayu.TestBeanEntry;
import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.HandlerResult;
import org.dayu.common.data.HandlerStatus;
import org.dayu.common.data.mr.MRApplicationData;
import org.dayu.common.model.YarnApplication;
import org.dayu.core.data.MRDataAnswer;
import org.dayu.core.http.HttpCallService;
import org.dayu.core.queue.MessageQueue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Sean Liu
 * @date 2019-07-23
 */

@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class MRFetcherTest {

  @Autowired
  private MRFetcher mrFetcher;

  @MockBean
  private HttpCallService httpCallService;

  @MockBean
  private MessageQueue messageQueue;

  @Before
  public void before() throws IOException {
    when(httpCallService.doGet(anyString())).thenAnswer(new MRDataAnswer());
  }

  @Test
  public void testFetch() {
    ApplicationData applicationData = new MRApplicationData();
    YarnApplication application = new YarnApplication();
    application.setId("application_1326381300833");
    applicationData.setApplication(application);
    HandlerResult result = mrFetcher.handle(applicationData);
    log.info("handle handlerStatus : {}", result);
    log.debug(JSON.toJSONString(applicationData, true));
    assert HandlerStatus.SUCCESSED.equals(result.getHandlerStatus());
  }

}
