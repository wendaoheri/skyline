package org.dayu.plugin.schedule;


/**
 * @author sean
 */
public interface SchedulePlugin {

  /**
   * get unique scheduleId by applicationId
   *
   * return null if cannot find any matched schedule
   *
   * @param applicationId
   * @return
   */
  String getScheduleIdByApplicationId(String applicationId);

}
