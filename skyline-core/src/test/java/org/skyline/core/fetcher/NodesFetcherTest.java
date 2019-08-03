package org.skyline.core.fetcher;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyline.core.TestBeanEntry;
import org.skyline.core.data.NodesAnswer;
import org.skyline.core.http.HttpCallService;
import org.skyline.core.queue.MessageQueue;
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
public class NodesFetcherTest {

  @MockBean
  private HttpCallService httpCallService;

  @MockBean
  private MessageQueue messageQueue;

  @Autowired
  private NodesFetcher fetcher;

  @Before
  public void before() throws IOException {
    when(httpCallService.doGet(anyString())).thenAnswer(new NodesAnswer());
  }

  @Test
  public void testFetch(){
    fetcher.fetch();
  }

}
