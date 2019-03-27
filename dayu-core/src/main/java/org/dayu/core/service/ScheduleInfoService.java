package org.dayu.core.service;

import java.util.Set;

/**
 * @author Sean Liu
 * @date 2019-03-27
 */
public interface ScheduleInfoService {

  void saveScheduleInfos(Set<String> scheduleIds);
}
