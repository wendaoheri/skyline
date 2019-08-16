package org.skyline.core.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.skyline.common.data.Records;
import org.skyline.common.data.RuntimeConfig;
import org.skyline.common.data.ScheduleInfo;
import org.skyline.common.data.YarnApplication;
import org.skyline.common.message.Message;
import org.skyline.common.message.MessageType;
import org.skyline.core.dto.ScrolledPageResult;
import org.skyline.core.dto.SearchRequest;
import org.skyline.core.handler.MessageSerdes;
import org.skyline.core.queue.MessageQueue;
import org.skyline.core.service.YarnApplicationService;
import org.skyline.core.storage.IStorage;
import org.skyline.plugin.schedule.ScheduleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ScrolledPage;
import org.springframework.stereotype.Service;

/**
 * @author sean
 */
@Service
@Slf4j
public class YarnApplicationServiceImpl implements YarnApplicationService {

  @Autowired
  private IStorage storage;

  @Autowired
  private MessageQueue messageQueue;

  @Autowired
  private MessageSerdes serdes;

  @Override
  public long getLastFetchTime() {

    RuntimeConfig rc = storage.findById(RuntimeConfig.INDEX_NAME, RuntimeConfig.TYPE_NAME,
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
    storage.upsert(RuntimeConfig.INDEX_NAME, RuntimeConfig.TYPE_NAME, Records.fromObject(rc));
  }

  @Override
  public void saveApplicationList(List<YarnApplication> apps) {
    saveApplicationList(YarnApplication.INDEX_NAME, YarnApplication.TYPE_NAME, apps);
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
    String indexName = String.format(YarnApplication.INDEX_NAME + "_trace-%s",
        DateFormatUtils.format(new Date(), "yyyyMMdd"));

    saveApplicationList(indexName, YarnApplication.TYPE_NAME, apps);
  }


  @Override
  public List<YarnApplication> getWithoutScheduleInfo(long begin, long end) {
    // TODO should sort by ts desc
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
    return storage.findByDSL(YarnApplication.INDEX_NAME, YarnApplication.TYPE_NAME,
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
        .findById(YarnApplication.INDEX_NAME, YarnApplication.TYPE_NAME, applicationId,
            YarnApplication.class);
  }

  @Override
  public ScrolledPageResult<YarnApplication> search(SearchRequest request) {
    
    return storage.scrollSearch(YarnApplication.INDEX_NAME,YarnApplication.TYPE_NAME,request,YarnApplication.class);
  }


  @Override
  public int addScheduleInfo(Map<String, ScheduleTrigger> appSchMap,
      List<ScheduleInfo> newSchedules) {
    Set<String> newScheduleIds =
        newSchedules == null ? null : newSchedules.parallelStream().map(x -> x.getScheduleId())
            .collect(Collectors.toSet());
    List<YarnApplication> apps = Lists.newArrayList();
    appSchMap.entrySet()
        .forEach(x -> {
          ScheduleTrigger st = x.getValue();
          YarnApplication app = new YarnApplication();
          app.setId(x.getKey());
          app.setScheduleId(st.getScheduleId());
          app.setJobId(st.getJobId());
          app.setTriggerId(x.getValue().getTriggerId());
          if (newScheduleIds != null && newScheduleIds.contains(st.getScheduleId())) {
            app.setNewSchedule(1);
          }
          apps.add(app);
        });
    try {
      storage.bulkUpsert(YarnApplication.INDEX_NAME, YarnApplication.TYPE_NAME,
          Records.fromObject(apps));
    } catch (ExecutionException e) {
      log.error(e.getMessage());
    } catch (InterruptedException e) {
      log.error(e.getMessage());
    }
    return 0;
  }

  @Override
  public void sendApplicationListToMQ(List<YarnApplication> apps) {
    Map<String, String> messages = Maps.newHashMap();
    Message<YarnApplication> m;
    for (YarnApplication app : apps) {
      m = new Message();
      m.setMessageContent(app);
      m.setMessageType(MessageType.APPLICATION_FETCH);
      messages.put(app.getApplicationId(), serdes.serialize(m));
    }
    messageQueue.sendMessage(messages);
  }
}
