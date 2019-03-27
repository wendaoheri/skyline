package org.dayu.core.service;

import java.util.List;
import java.util.Map;
import org.dayu.core.dto.SearchRequestDTO;
import org.dayu.core.model.YarnApplication;
import org.springframework.data.domain.Page;

/**
 * @author Sean Liu
 */
public interface YarnApplicationService {

  String TASK_FETCHER_LAST_FETCH_TIME_KEY = "last_fetch_time";

  long getLastFetchTime();

  void setLastFetchTime(long lastFetchTime);

  void saveApplicationList(List<YarnApplication> apps);

  List<YarnApplication> getWithoutScheduleInfo(long begin, long end);


  List<YarnApplication> getApplications(long begin, long end);

  YarnApplication getApplicationById(String applicationId);

  Page<YarnApplication> search(SearchRequestDTO request);

  int setScheduleInfo(Map<String, String> appSchMap);
}
