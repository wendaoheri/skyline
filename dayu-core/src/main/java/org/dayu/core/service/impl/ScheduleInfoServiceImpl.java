package org.dayu.core.service.impl;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.dayu.core.model.ScheduleInfo;
import org.dayu.core.repository.ScheduleInfoRepository;
import org.dayu.core.service.ScheduleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Sean Liu
 * @date 2019-03-27
 */
@Service
@Slf4j
public class ScheduleInfoServiceImpl implements ScheduleInfoService {

  @Autowired
  private ScheduleInfoRepository scheduleInfoRepository;

  @Override
  public void saveScheduleInfos(Set<String> scheduleIds) {
    Set<String> existsIds = scheduleInfoRepository.findByScheduleIdIn(scheduleIds).parallelStream()
        .map(x -> x.getScheduleId()).collect(Collectors.toSet());
    scheduleIds.removeAll(existsIds);
    log.debug("save schedule ids : {}", scheduleIds);
    List<ScheduleInfo> toSaved = scheduleIds.parallelStream().map(scheduleId -> {
      ScheduleInfo si = new ScheduleInfo();
      si.setScheduleId(scheduleId);
      return si;
    }).collect(Collectors.toList());

    scheduleInfoRepository.saveAll(toSaved);
  }
}
