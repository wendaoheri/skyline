package org.skyline.core.http;

import java.io.IOException;
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
 * @date 2019-08-20
 */
@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class HadoopHACallServiceTest extends MockBeanTest {

  @Autowired
  private HadoopHACallService callService;

  @Test
  public void testDoGet() throws IOException {
    callService.doGet("host1:8088", "/ws/v1");
  }

}
