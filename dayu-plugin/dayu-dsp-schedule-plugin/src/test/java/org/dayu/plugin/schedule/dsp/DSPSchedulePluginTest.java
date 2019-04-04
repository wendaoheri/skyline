package org.dayu.plugin.schedule.dsp;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Sean Liu
 * @date 2019-04-02
 */
@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
@EnableScheduling
@EnableCaching
public class DSPSchedulePluginTest {

  @Autowired
  private DSPSchedulePlugin dspSchedulePlugin;

  @Test
  public void testGetScheduleIdByApplicationId() {
    String scheduleId = dspSchedulePlugin
        .getScheduleIdByApplicationId("application_1547782571903_0664");
    log.info(String.format("scheduleId is : [%s]", scheduleId));
  }

}
