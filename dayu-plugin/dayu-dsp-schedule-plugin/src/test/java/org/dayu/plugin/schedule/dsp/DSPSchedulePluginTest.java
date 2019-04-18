package org.dayu.plugin.schedule.dsp;

import lombok.extern.slf4j.Slf4j;
import org.dayu.plugin.schedule.ScheduleTrigger;
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
  public void testGetScheduleIdByApplicationId() throws InterruptedException {
    Thread.sleep(3000);
    ScheduleTrigger st = dspSchedulePlugin
        .getScheduleIdByApplicationId("application_1554947148219_5071");
    log.info(String.format("scheduleId is : [%s]", String.valueOf(st)));
  }

}
