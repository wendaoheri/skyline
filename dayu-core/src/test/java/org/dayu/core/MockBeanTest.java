package org.dayu.core;

import org.dayu.core.queue.MessageQueue;
import org.dayu.core.storage.IStorage;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
public class MockBeanTest {

  @MockBean
  private IStorage storage;

  @MockBean
  private MessageQueue messageQueue;

}
