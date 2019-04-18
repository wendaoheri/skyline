package org.dayu.plugin.schedule;

import lombok.Data;

/**
 * represent a running period of scheduler
 *
 * @author Sean Liu
 * @date 2019-04-18
 */
@Data
public class ScheduleTrigger {

  private String scheduleId;
  private String triggerId;

}
