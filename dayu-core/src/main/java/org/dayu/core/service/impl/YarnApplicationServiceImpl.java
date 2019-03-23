package org.dayu.core.service.impl;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.dayu.core.model.ApplicationScheduleInfo;
import org.dayu.core.model.RuntimeConfig;
import org.dayu.core.model.YarnApplication;
import org.dayu.core.repository.ApplicationScheduleInfoRepository;
import org.dayu.core.repository.RuntimeConfigRepository;
import org.dayu.core.repository.YarnApplicationRepository;
import org.dayu.core.service.YarnApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author sean
 */
@Service
@Slf4j
public class YarnApplicationServiceImpl implements YarnApplicationService {

  @Autowired
  private RuntimeConfigRepository runtimeConfigRepository;

  @Autowired
  private YarnApplicationRepository yarnApplicationRepository;

  @Autowired
  private ApplicationScheduleInfoRepository applicationScheduleInfoRepository;

  @Override
  public long getLastFetchTime() {
    RuntimeConfig rc = runtimeConfigRepository
        .findByRuntimeKey(TASK_FETCHER_LAST_FETCH_TIME_KEY);
    if (null != rc) {
      String lastFetchTimeStr = rc.getRuntimeValue();
      return Long.parseLong(lastFetchTimeStr);
    }
    return 0;
  }

  @Override
  public void setLastFetchTime(long lastFetchTime) {
    String lastFetchTimeStr = String.valueOf(lastFetchTime);
    RuntimeConfig rc = runtimeConfigRepository
        .findByRuntimeKey(TASK_FETCHER_LAST_FETCH_TIME_KEY);
    if (null != rc) {
      rc.setRuntimeValue(lastFetchTimeStr);
    } else {
      rc = new RuntimeConfig();
      rc.setRuntimeKey(TASK_FETCHER_LAST_FETCH_TIME_KEY);
      rc.setRuntimeValue(lastFetchTimeStr);
    }
    runtimeConfigRepository.save(rc);
  }

  @Override
  public void saveApplicationList(List<YarnApplication> apps) {
    yarnApplicationRepository.saveAll(apps);
    log.info("Saved application list success : {}", apps.size());
  }

  @Override
  public List<String> getIdsWithoutScheduleInfo(long begin, long end) {
    return yarnApplicationRepository.findIdsWithoutScheduleInfo(begin, end);
  }

  @Override
  public void saveApplicationScheduleInfoList(List<ApplicationScheduleInfo> asList) {
    applicationScheduleInfoRepository.saveAll(asList);
  }

  @Override
  public List<YarnApplication> getApplications(long begin, long end) {
    yarnApplicationRepository.findAll((Specification<YarnApplication>) (root, query, cb) -> {
      Predicate p1 = cb.like(root.get("name"), "%ray%");
      Predicate p2 = cb.greaterThan(root.get("rid"),"3");

      return null;
    });
    return yarnApplicationRepository.findByFinishedTimeBetween(begin, end);
  }

  @Override
  public YarnApplication getApplicationById(String applicationId) {
    return yarnApplicationRepository.findById(applicationId).get();
  }
}
