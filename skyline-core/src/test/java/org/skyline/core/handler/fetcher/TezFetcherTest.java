package org.skyline.core.handler.fetcher;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyline.common.data.YarnApplication;
import org.skyline.common.data.tez.TezApplicationData;
import org.skyline.core.MockBeanTest;
import org.skyline.core.TestBeanEntry;
import org.skyline.core.data.TezDataAnswer;
import org.skyline.core.http.HttpCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Sean Liu
 * @date 2019-08-09
 */
@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class TezFetcherTest extends MockBeanTest {

  @Autowired
  private TezFetcher tezFetcher;

  @MockBean
  private HttpCallService httpCallService;

  String applicationId = "application_1562840950978_1740308";
  String dagId = "dag_1562840950978_1740308_1";

  @Before
  public void before() throws IOException {
    when(httpCallService.doGet(anyString())).thenAnswer(new TezDataAnswer());
  }

  @Test
  public void testFetch() {
    TezApplicationData tezApplicationData = new TezApplicationData();
    YarnApplication application = new YarnApplication();
    application.setApplicationId(applicationId);
    tezApplicationData.setApplication(application);

    tezFetcher.handle(tezApplicationData);

  }

  @Test
  public void testMockFetch() throws IOException {
    String tezApplicationUrl = tezFetcher.getTezTaskUrl(dagId);
    JSONObject tezApplication = tezFetcher.getDataFromTLS(tezApplicationUrl);
    log.info("Tez application : {}", tezApplication);
  }

  @Test
  public void testUrl() {

    log.info("DAG url : {}", tezFetcher.getTezDagUrl(applicationId));
    log.info("Application url : {}", tezFetcher.getTezApplicationUrl(applicationId));
    log.info("Vertex url : {}", tezFetcher.getTezVertexUrl(dagId));
    log.info("Task url : {}", tezFetcher.getTezTaskUrl(dagId));
    log.info("Task attempt url : {}", tezFetcher.getTezTaskAttemptUrl(dagId));
  }

}
