package org.dayu.core.drools;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.dayu.TestBeanEntry;
import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.mr.MRApplicationData;
import org.dayu.common.model.YarnApplication;
import org.dayu.core.MockBeanTest;
import org.dayu.core.data.MRDataAnswer;
import org.dayu.core.handler.fetcher.MRFetcher;
import org.dayu.core.http.HttpCallService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Sean Liu
 * @date 2019-07-31
 */
@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class DroolsTest extends MockBeanTest {

  @Autowired
  private MRFetcher mrFetcher;

  @MockBean
  private HttpCallService httpCallService;
  @Autowired
  private KieSession kieSession;

  @Before
  public void before() throws IOException {
    when(httpCallService.doGet(anyString())).thenAnswer(new MRDataAnswer());
  }

  @Test
  public void test() {
    // construct mock data
    ApplicationData applicationData = new MRApplicationData();
    YarnApplication application = new YarnApplication();
    application.setId("application_1326381300833");
    applicationData.setApplication(application);
    mrFetcher.handle(applicationData);

    // test
    kieSession.insert(applicationData);
    int count = kieSession.fireAllRules();
    log.info("fired rule count : {}", count);
  }
}
