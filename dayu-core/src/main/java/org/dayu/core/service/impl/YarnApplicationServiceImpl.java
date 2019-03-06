package org.dayu.core.service.impl;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.dayu.core.model.ApplicationScheduleInfo;
import org.dayu.core.model.RuntimeConfig;
import org.dayu.core.model.YarnApplication;
import org.dayu.core.repository.ApplicationScheduleInfoRepository;
import org.dayu.core.repository.RuntimeConfigRepository;
import org.dayu.core.repository.YarnApplicationRepository;
import org.dayu.core.service.YarnApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
