package org.dayu.core.service.impl;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
public class ScheduleInfoServiceImpl implements ScheduleInfoService {

  @Autowired
  private ScheduleInfoRepository scheduleInfoRepository;

  @Override
  public void saveScheduleInfos(Set<String> scheduleIds) {
    Set<String> existsIds = Sets
        .newHashSet(scheduleInfoRepository.findScheduleIdByScheduleIdIn(scheduleIds));
    scheduleIds.removeAll(existsIds);
    List<ScheduleInfo> toSaved = scheduleIds.parallelStream().map(scheduleId -> {
      ScheduleInfo si = new ScheduleInfo();
      si.setScheduleId(scheduleId);
      return si;
    }).collect(Collectors.toList());

    scheduleInfoRepository.saveAll(toSaved);
  }
}
