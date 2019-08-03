package org.skyline.core.schedule;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.skyline.common.data.ScheduleInfo;
import org.skyline.common.data.YarnApplication;
import org.skyline.core.service.ScheduleInfoService;
import org.skyline.core.service.YarnApplicationService;
import org.skyline.plugin.schedule.SchedulePlugin;
import org.skyline.plugin.schedule.ScheduleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Sean Liu
 */
@Service
@Slf4j
public class ScheduleInfoRunner {

  @Autowired
  private SchedulePlugin schedulePlugin;

  @Autowired
  private YarnApplicationService yarnApplicationService;

  @Autowired
  private ScheduleInfoService scheduleInfoService;

  @Value("${skyline.plugin.schedule.min_delay_second}")
  private long minDelaySecond;

  @Value("${skyline.plugin.schedule.max_delay_second}")
  private long maxDelaySecond;

  @Scheduled(fixedRate = 60000)
  public void addScheduleInfo() {

    long curr = System.currentTimeMillis();
    long begin = curr - maxDelaySecond * 1000;
    long end = curr - minDelaySecond * 1000;

    List<YarnApplication> apps = yarnApplicationService
        .getWithoutScheduleInfo(begin, end);

    log.info("Got yarn application without schedule info between {} and {} size : {}", begin, end,
        apps.size());

    // applicationId -> scheduleId
    Map<String, ScheduleTrigger> appSchMap = Maps.newHashMap();
    apps.forEach(app -> {
      ScheduleTrigger scheduleTrigger = schedulePlugin
          .getScheduleIdByApplicationId(app.getId());
      if (scheduleTrigger != null) {
        appSchMap.put(app.getId(), scheduleTrigger);
      }
    });
    Set<String> scheduleIds = appSchMap.values().parallelStream().map(x -> x.getScheduleId())
        .collect(Collectors.toSet());
    // 第一次出现的schedule
    List<ScheduleInfo> newSchedules = scheduleInfoService.saveScheduleInfos(scheduleIds);

    log.info("application schedule map size : {}", appSchMap.size());

    yarnApplicationService.addScheduleInfo(appSchMap, newSchedules);
  }
}
