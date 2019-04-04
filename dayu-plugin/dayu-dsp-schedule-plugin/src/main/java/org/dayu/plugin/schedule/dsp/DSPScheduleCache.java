package org.dayu.plugin.schedule.dsp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author Sean Liu
 * @date 2019-04-02
 */
@Service
@Slf4j
public class DSPScheduleCache {

  private static final String CACHE_NAME = "job_application_log";

  @Cacheable(cacheNames = CACHE_NAME, key = "#p0")
  public String getScheduleIdByApplicationIdWithCache(String applicationId) {
    return null;
  }

  @CachePut(cacheNames = CACHE_NAME, key = "#p0")
  public String putCache(String applicationId, String jobId) {
    log.info("put cache applicationId [{}] -> jobId [{}]", applicationId, jobId);
    return jobId;
  }

}
