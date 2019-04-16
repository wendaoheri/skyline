package org.dayu.core.service;

import java.util.List;
import java.util.Set;
import org.dayu.core.model.ScheduleInfo;

/**
 * @author Sean Liu
 * @date 2019-03-27
 */
public interface ScheduleInfoService {

  List<ScheduleInfo> saveScheduleInfos(Set<String> scheduleIds);
}
