package org.dayu.plugin.schedule.dsp.repository;

import java.util.Date;
import java.util.List;
import org.dayu.plugin.schedule.dsp.model.JobApplicationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sean Liu
 * @date 2019-04-02
 */
@Repository
public interface JobApplicationLogRepository extends JpaRepository<JobApplicationLog, String> {

  JobApplicationLog findJobApplicationLogByApplicationLogLike(String applicationId);

  List<JobApplicationLog> findJobApplicationLogsByJobFrequencyGreaterThanEqual(long jobFrequency);

  List<JobApplicationLog> findJobApplicationLogsByCreatedTimeGreaterThanEqual(Date date);
}
