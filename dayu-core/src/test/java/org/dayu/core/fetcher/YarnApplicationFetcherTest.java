package org.dayu.core.fetcher;

import lombok.extern.slf4j.Slf4j;
import org.dayu.TestBeanEntry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class YarnApplicationFetcherTest {

  @Autowired
  private YarnApplicationFetcher yarnApplicationFetcher;

  @Test
  public void testFetch() {
    yarnApplicationFetcher.fetch();
  }

  @Test
  public void testFetchRunningApplications() {
    yarnApplicationFetcher.fetchUnfinishedApplications();
  }

  @Test
  public void testGetFetchTaskUrl() {
    String fetchTaskUrl = yarnApplicationFetcher.getAppListUrl(123, 456, 0);
    log.info(fetchTaskUrl);
  }


}
