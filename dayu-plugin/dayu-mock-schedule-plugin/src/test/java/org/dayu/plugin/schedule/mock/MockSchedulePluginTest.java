package org.dayu.plugin.schedule.mock;

import lombok.extern.slf4j.Slf4j;
import org.dayu.plugin.schedule.SchedulePlugin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class MockSchedulePluginTest {


  private SchedulePlugin mockSchedulePlugin;

  @Test
  public void testGetScheduleIdByApplicationId() {
    String scheduleId = mockSchedulePlugin.getScheduleIdByApplicationId("aaaa");
    log.info("ScheduleId for applicationId {} : {}", "aaaa", scheduleId);
  }


}
