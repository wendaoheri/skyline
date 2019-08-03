package org.skyline.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyline.core.MockBeanTest;
import org.skyline.core.TestBeanEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class MessageUtilsTest extends MockBeanTest {

  @Autowired
  private MessageUtils messageUtils;

  @Test
  public void testGetMessage() {
    String message = messageUtils
        .getMessage("describe.org.skyline.core.handler.advisor.mr.MapperElapsedSkewAdvisor");
    System.out.println(message);
  }

}
