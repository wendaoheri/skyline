package org.dayu.core.service.impl;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.dayu.common.model.Records;
import org.dayu.common.model.ScheduleInfo;
import org.dayu.core.service.ScheduleInfoService;
import org.dayu.storage.IStorage;
import org.elasticsearch.index.IndexNotFoundException;
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
  private IStorage storage;

  @Override
  public List<ScheduleInfo> saveScheduleInfos(Set<String> scheduleIds) {
    if(scheduleIds == null || scheduleIds.size() == 0){
      return null;
    }
    Set<String> existsIds = null;
    try {
      List<ScheduleInfo> scheduleInfoList = storage
          .findByIds(ScheduleInfo.DATABASE_NAME, ScheduleInfo.TABLE_NAME, scheduleIds,
              ScheduleInfo.class);
      existsIds = scheduleInfoList.parallelStream().map(x -> x.getScheduleId())
          .collect(Collectors.toSet());
    } catch (IndexNotFoundException e) {
      log.info("index not found");
    }
    if (existsIds != null) {
      scheduleIds.removeAll(existsIds);
    }

    log.info("save schedule ids : {}", scheduleIds);
    List<ScheduleInfo> toSaved = scheduleIds.parallelStream().map(scheduleId -> {
      ScheduleInfo si = new ScheduleInfo();
      si.setScheduleId(scheduleId);
      return si;
    }).collect(Collectors.toList());

    try {
      storage.bulkUpsert(ScheduleInfo.DATABASE_NAME, ScheduleInfo.TABLE_NAME,
          Records.fromObject(toSaved));
    } catch (ExecutionException e) {
      log.error(e.getMessage());
    } catch (InterruptedException e) {
      log.error(e.getMessage());
    }
    return toSaved;
  }
}
