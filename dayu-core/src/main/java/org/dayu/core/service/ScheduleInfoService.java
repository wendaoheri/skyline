package org.dayu.core.service;

import java.util.List;
import java.util.Set;
import org.dayu.common.data.ScheduleInfo;

/**
 * @author Sean Liu
 * @date 2019-03-27
 */
public interface ScheduleInfoService {

  /**
   * return not existed schedules
   */
  List<ScheduleInfo> saveScheduleInfos(Set<String> scheduleIds);
}
