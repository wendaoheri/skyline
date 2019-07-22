package org.dayu.core.fetcher;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.dayu.TestBeanEntry;
import org.dayu.core.data.YarnMetricsAnswer;
import org.dayu.core.http.HttpCallService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Sean Liu
 * @date 2019-07-22
 */
@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class YarnMetricsFetcherTest {

  @MockBean
  private HttpCallService httpCallService;

  @Autowired
  private YarnMetricsFetcher fetcher;

  @Before
  public void before() throws IOException {
    when(httpCallService.doGet(anyString())).thenAnswer(new YarnMetricsAnswer());
  }

  @Test
  public void testFetch() {
    fetcher.fetch();
  }
}
