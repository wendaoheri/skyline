package org.dayu.core.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.dayu.TestBeanEntry;
import org.dayu.core.model.YarnApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class YarnApplicationServiceTest {

  @Autowired
  private YarnApplicationService yarnApplicationService;

  @Test
  public void testGetApplications() {
    List<YarnApplication> applications = yarnApplicationService
        .getApplications(1551863919496L, 1551864708358L);

    for (YarnApplication ya : applications) {
      log.info(ya.toString());
    }
  }

}
