package org.dayu.plugin.schedule.dsp;

import com.google.common.collect.Sets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.dayu.plugin.schedule.SchedulePlugin;
import org.dayu.plugin.schedule.dsp.model.JobApplicationLog;
import org.dayu.plugin.schedule.dsp.repository.JobApplicationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Sean Liu
 * @date 2019-04-02
 */
@Slf4j
public class DSPSchedulePlugin implements SchedulePlugin {


  private static final String LOG_SPLIT = ",";
  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd0000");

  @Autowired
  private DSPScheduleCache cache;

  @Autowired
  private JobApplicationLogRepository jobApplicationLogRepository;

  @Override
  public String getScheduleIdByApplicationId(String applicationId) {
    return cache.getScheduleIdByApplicationIdWithCache(applicationId);
  }

  @Scheduled(fixedRate = 10000)
  public void refresh() {
    log.info("start refresh caches");
    Date yest = new Date(System.currentTimeMillis() - 86400000L);
    long todayFreq = Long.parseLong(sdf.format(yest));
    log.info("start frequency : {}", todayFreq);
    List<JobApplicationLog> jobs = jobApplicationLogRepository
        .findJobApplicationLogsByJobFrequencyGreaterThanEqual(todayFreq);
    putCaches(jobs);
    log.info("end refresh caches");
  }

  public void putCaches(List<JobApplicationLog> jobApplicationLogs) {
    log.info("Start add to cache application size : {}", jobApplicationLogs.size());
    jobApplicationLogs.forEach(job -> {
      String jobId = String.valueOf(job.getJobId());
      String applicationLog = job.getApplicationLog();
      Set<String> applicationIdSet = Sets.newHashSet(applicationLog.split(LOG_SPLIT));
      for (String applicationId : applicationIdSet) {
        if (cache.getScheduleIdByApplicationIdWithCache(applicationId) == null) {
          cache.putCache(applicationId, jobId);
          log.info(cache.getScheduleIdByApplicationIdWithCache(applicationId));
        }
      }
    });
    log.info("End add to cache application size : {}", jobApplicationLogs.size());
  }

}
