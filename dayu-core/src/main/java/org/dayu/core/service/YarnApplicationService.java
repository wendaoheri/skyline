package org.dayu.core.service;

import java.util.List;
import org.dayu.core.dto.SearchRequestDTO;
import org.dayu.core.model.ApplicationScheduleInfo;
import org.dayu.core.model.YarnApplication;

public interface YarnApplicationService {

  String TASK_FETCHER_LAST_FETCH_TIME_KEY = "last_fetch_time";

  long getLastFetchTime();

  void setLastFetchTime(long lastFetchTime);

  void saveApplicationList(List<YarnApplication> apps);

  List<String> getIdsWithoutScheduleInfo(long begin, long end);

  void saveApplicationScheduleInfoList(List<ApplicationScheduleInfo> asList);

  List<YarnApplication> getApplications(long begin, long end);

  YarnApplication getApplicationById(String applicationId);

  List<YarnApplication> search(SearchRequestDTO request);
}
