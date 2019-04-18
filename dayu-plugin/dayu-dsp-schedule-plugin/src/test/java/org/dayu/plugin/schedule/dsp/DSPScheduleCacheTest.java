package org.dayu.plugin.schedule.dsp;

import lombok.extern.slf4j.Slf4j;
import org.dayu.plugin.schedule.ScheduleTrigger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Sean Liu
 * @date 2019-04-02
 */
@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
@EnableCaching
public class DSPScheduleCacheTest {

  @Autowired
  private DSPScheduleCache cache;

  @Test
  public void testGetScheduleIdByApplicationIdWithCache() throws InterruptedException {
//    JobApplicationLog job = jobApplicationLogRepository
//        .findJobApplicationLogByApplicationLogLike("%application_1547782571903_0664%");
    ScheduleTrigger st = new ScheduleTrigger();

    st.setScheduleId("dddddd");
    st.setTriggerId("ttttt");
    cache.putCache("application_1547782571903_0664", st);

    ScheduleTrigger stCached = cache
        .getScheduleIdByApplicationIdWithCache("application_1547782571903_0664");
    log.info(stCached.toString());

    stCached = cache.getScheduleIdByApplicationIdWithCache("application_1547782571903_066");
    log.info(String.valueOf(stCached));
  }

}
