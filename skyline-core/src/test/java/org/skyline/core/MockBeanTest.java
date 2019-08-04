package org.skyline.core;

import org.skyline.core.queue.MessageQueue;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
public class MockBeanTest {

  @MockBean
  private MessageQueue messageQueue;

}
