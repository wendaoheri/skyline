package org.dayu.core.service.impl;

import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.dayu.common.model.Records;
import org.dayu.common.model.RuntimeConfig;
import org.dayu.common.model.ScheduleInfo;
import org.dayu.common.model.YarnApplication;
import org.dayu.core.dto.SearchRequestDTO;
import org.dayu.core.service.YarnApplicationService;
import org.dayu.storage.IStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * @author sean
 */
@Service
@Slf4j
public class YarnApplicationServiceImpl implements YarnApplicationService {

  @Autowired
  private IStorage storage;

  @Override
  public long getLastFetchTime() {

    RuntimeConfig rc = storage.findById(RuntimeConfig.DATABASE_NAME, RuntimeConfig.TABLE_NAME,
        TASK_FETCHER_LAST_FETCH_TIME_KEY, RuntimeConfig.class);

    if (null != rc) {
      String lastFetchTimeStr = rc.getValue();
      return Long.parseLong(lastFetchTimeStr);
    }
    return 0;
  }

  @Override
  public void setLastFetchTime(long lastFetchTime) {
    String lastFetchTimeStr = String.valueOf(lastFetchTime);
    RuntimeConfig rc = new RuntimeConfig();
    rc.setId(TASK_FETCHER_LAST_FETCH_TIME_KEY);
    rc.setValue(lastFetchTimeStr);
    storage.upsert(RuntimeConfig.DATABASE_NAME, RuntimeConfig.TABLE_NAME, Records.fromObject(rc));
  }

  @Override
  public void saveApplicationList(List<YarnApplication> apps) {
    saveApplicationList(YarnApplication.DATABASE_NAME, YarnApplication.TABLE_NAME, apps);
  }

  @Override
  public void saveApplicationList(String databaseName, String tableName,
      List<YarnApplication> apps) {
    try {
      if (apps != null && apps.size() > 0) {
        storage.bulkUpsert(databaseName, tableName, Records.fromObject(apps));
        log.info("Saved application list success : {}", apps.size());
      }
    } catch (ExecutionException e) {
      log.error(e.getMessage());
    } catch (InterruptedException e) {
      log.error(e.getMessage());
    }
  }

  @Override
  public void saveApplicationListTrace(List<YarnApplication> apps) {
    String indexName = String.format(YarnApplication.DATABASE_NAME + "_trace-%s",
        DateFormatUtils.format(new Date(), "yyyyMMdd"));

    saveApplicationList(indexName, YarnApplication.TABLE_NAME, apps);
  }


  @Override
  public List<YarnApplication> getWithoutScheduleInfo(long begin, long end) {
    String dsl = "{\n"
        + "    \"bool\": {\n"
        + "      \"must_not\": [\n"
        + "        {\n"
        + "          \"exists\": {\n"
        + "            \"field\": \"schedule_id\"\n"
        + "          }\n"
        + "        }\n"
        + "      ],\n"
        + "      \"must\": [\n"
        + "        {\n"
        + "          \"range\": {\n"
        + "            \"finished_time\": {\n"
        + "              \"gte\": %d,\n"
        + "              \"lte\": %d\n"
        + "            }\n"
        + "          }\n"
        + "        }\n"
        + "      ]\n"
        + "    }\n"
        + "  }";
    return storage.findByDSL(YarnApplication.DATABASE_NAME, YarnApplication.TABLE_NAME,
        String.format(dsl, begin, end), YarnApplication.class);
  }

  @Override
  public List<YarnApplication> getApplications(long begin, long end) {
    return null;
//    return yarnApplicationRepository.findByFinishedTimeBetween(begin, end);
  }

  @Override
  public YarnApplication getApplicationById(String applicationId) {
    return storage
        .findById(YarnApplication.DATABASE_NAME, YarnApplication.TABLE_NAME, applicationId,
            YarnApplication.class);
  }

  @Override
  public Page<YarnApplication> search(SearchRequestDTO request) {

    return null;
  }


  @Override
  public int addScheduleInfo(Map<String, String> appSchMap,
      List<ScheduleInfo> newSchedules) {
    Set<String> newScheduleIds =
        newSchedules == null ? null : newSchedules.parallelStream().map(x -> x.getScheduleId())
            .collect(Collectors.toSet());
    List<YarnApplication> apps = Lists.newArrayList();
    appSchMap.entrySet()
        .forEach(x -> {
          YarnApplication app = new YarnApplication();
          app.setId(x.getKey());
          app.setScheduleId(x.getValue());
          if (newScheduleIds != null && newScheduleIds.contains(x.getValue())) {
            app.setNewSchedule(1);
          }
          apps.add(app);
        });
    try {
      storage.bulkUpsert(YarnApplication.DATABASE_NAME, YarnApplication.TABLE_NAME,
          Records.fromObject(apps));
    } catch (ExecutionException e) {
      log.error(e.getMessage());
    } catch (InterruptedException e) {
      log.error(e.getMessage());
    }
    return 0;
  }
}
