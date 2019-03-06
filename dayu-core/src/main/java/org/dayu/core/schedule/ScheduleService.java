package org.dayu.core.schedule;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dayu.core.model.ApplicationScheduleInfo;
import org.dayu.core.model.ApplicationScheduleInfo.ApplicationScheduleId;
import org.dayu.core.service.YarnApplicationService;
import org.dayu.plugin.schedule.SchedulePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ScheduleService {

  @Autowired
  private SchedulePlugin schedulePlugin;

  @Autowired
  private YarnApplicationService yarnApplicationService;

  @Value("${dayu.plugin.schedule.min_delay_second}")
  private long minDelaySecond;

  @Value("${dayu.plugin.schedule.max_delay_second}")
  private long maxDelaySecond;

  public void addScheduleInfo() {

    long curr = System.currentTimeMillis();
    long begin = curr - maxDelaySecond * 1000;
    long end = curr - minDelaySecond * 1000;

    List<String> ids = yarnApplicationService
        .getIdsWithoutScheduleInfo(begin, end);

    log.info("Got yarn application without schedule info between {} and {} size : {}", begin, end,
        ids.size());

    List<ApplicationScheduleInfo> asList = Lists.newArrayList();

    for (String id : ids) {
      String scheduleId = schedulePlugin.getScheduleIdByApplicationId(id);
      if (StringUtils.isNotEmpty(scheduleId)) {
        ApplicationScheduleInfo as = new ApplicationScheduleInfo();
        ApplicationScheduleId asId = new ApplicationScheduleId();
        asId.setApplicationId(id);
        asId.setScheduleId(scheduleId);
        as.setApplicationScheduleId(asId);

        asList.add(as);
      }
    }
    log.info("Got schedule info size : {}", asList.size());
    yarnApplicationService.saveApplicationScheduleInfoList(asList);
    log.info("Save schedule info size : {}", asList.size());
  }


}
