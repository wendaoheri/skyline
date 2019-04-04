package org.dayu.core.schedule;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dayu.core.model.YarnApplication;
import org.dayu.core.repository.ScheduleInfoRepository;
import org.dayu.core.service.ScheduleInfoService;
import org.dayu.core.service.YarnApplicationService;
import org.dayu.plugin.schedule.SchedulePlugin;
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

  @Value("${dayu.plugin.schedule.min_delay_second}")
  private long minDelaySecond;

  @Value("${dayu.plugin.schedule.max_delay_second}")
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
    Map<String, String> appSchMap = Maps.newHashMap();

    apps.forEach(app -> {
      String scheduleId = schedulePlugin.getScheduleIdByApplicationId(app.getId());
      if (StringUtils.isNotEmpty(scheduleId)) {
        appSchMap.put(app.getId(), scheduleId);
      }
    });
    Set<String> scheduleIds = Sets.newHashSet(appSchMap.values());
    scheduleInfoService.saveScheduleInfos(scheduleIds);
    log.info("application schedule map size : {}", appSchMap.size());
    yarnApplicationService.setScheduleInfo(appSchMap);
  }
}
