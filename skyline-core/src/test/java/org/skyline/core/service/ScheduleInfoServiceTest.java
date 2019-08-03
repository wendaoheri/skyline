package org.skyline.core.service;

import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyline.core.TestBeanEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Sean Liu
 * @date 2019-04-17
 */

@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class ScheduleInfoServiceTest {

  @Autowired
  private ScheduleInfoService scheduleInfoService;

  @Test
  public void testSaveScheduleInfos() {
    Set<String> ids = Sets.newHashSet();

    ids.add("id1");
    ids.add("id2");
    ids.add("id3");
    ids.add("id4");
    scheduleInfoService.saveScheduleInfos(ids);
  }
}
