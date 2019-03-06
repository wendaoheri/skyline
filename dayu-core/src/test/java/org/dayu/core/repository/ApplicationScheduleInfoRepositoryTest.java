package org.dayu.core.repository;


import lombok.extern.slf4j.Slf4j;
import org.dayu.TestBeanEntry;
import org.dayu.core.model.ApplicationScheduleInfo;
import org.dayu.core.model.ApplicationScheduleInfo.ApplicationScheduleId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class ApplicationScheduleInfoRepositoryTest {

  @Autowired
  private ApplicationScheduleInfoRepository applicationScheduleInfoRepository;

  @Test
  public void testAdd() {
    ApplicationScheduleInfo as = new ApplicationScheduleInfo();
    ApplicationScheduleId id = new ApplicationScheduleId();
    id.setApplicationId("app_id");
    id.setScheduleId("schedule_id");
    as.setApplicationScheduleId(id);
    applicationScheduleInfoRepository.save(as);
  }

}
