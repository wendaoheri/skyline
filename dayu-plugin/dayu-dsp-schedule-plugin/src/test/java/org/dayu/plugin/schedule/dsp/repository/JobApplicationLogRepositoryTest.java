package org.dayu.plugin.schedule.dsp.repository;

import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.dayu.plugin.schedule.dsp.TestBeanEntry;
import org.dayu.plugin.schedule.dsp.model.JobApplicationLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Sean Liu
 * @date 2019-04-02
 */
@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class JobApplicationLogRepositoryTest {

  @Autowired
  private JobApplicationLogRepository jobApplicationLogRepository;

  @Test
  public void test() {
    List<JobApplicationLog> list = jobApplicationLogRepository.findAll();
    log.info(list.toString());
  }

  @Test
  public void testFindJobApplicationLogByApplicationLogLike() {
    JobApplicationLog job = jobApplicationLogRepository
        .findJobApplicationLogByApplicationLogLike("%application_1547782571903_0664%");
    log.info(job.toString());
  }

  @Test
  public void testFindJobApplicationLogsByJobFrequencyAfter(){
    List<JobApplicationLog> jobs = jobApplicationLogRepository
        .findJobApplicationLogsByJobFrequencyGreaterThanEqual(201902270000L);
    log.info(jobs.toString());
  }

  @Test
  public void testFindJobApplicationLogsByCreatedTimeGreaterThanEqual(){
    List<JobApplicationLog> jobs = jobApplicationLogRepository
        .findJobApplicationLogsByCreatedTimeGreaterThanEqual(new Date(System.currentTimeMillis() - 86400000L));
    log.info(jobs.toString());
  }
}
