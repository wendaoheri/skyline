package org.dayu.core.service;

import java.util.List;
import java.util.Set;
import org.dayu.common.model.ScheduleInfo;

/**
 * @author Sean Liu
 * @date 2019-03-27
 */
public interface ScheduleInfoService {

  /**
   * return not existed schedules
   * @param scheduleIds
   * @return
   */
  List<ScheduleInfo> saveScheduleInfos(Set<String> scheduleIds);
}
