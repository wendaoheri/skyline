package org.skyline.core.handler.fetcher;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyline.common.data.ApplicationData;
import org.skyline.common.data.HandlerResult;
import org.skyline.common.data.HandlerStatus;
import org.skyline.common.data.YarnApplication;
import org.skyline.common.data.mr.MRApplicationData;
import org.skyline.core.TestBeanEntry;
import org.skyline.core.data.MRDataAnswer;
import org.skyline.core.http.HttpCallService;
import org.skyline.core.queue.MessageQueue;
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
    log.info(JSON.toJSONString(applicationData, true));
    assert HandlerStatus.SUCCESSED.equals(result.getHandlerStatus());
  }

}
