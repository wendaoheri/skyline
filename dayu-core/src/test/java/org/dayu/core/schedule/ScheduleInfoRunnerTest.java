package org.dayu.core.schedule;

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
public class ScheduleInfoRunnerTest {

  @Autowired
  private ScheduleInfoRunner scheduleInfoRunner;

  @Test
  public void testAddScheduleInfo() {
    scheduleInfoRunner.addScheduleInfo();
  }

}
