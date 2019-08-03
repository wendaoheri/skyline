package org.dayu.common.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-04-17
 */
@Data
public class ScheduleInfo {

  public static final String DATABASE_NAME = "schedule_info";

  public static final String TABLE_NAME = "_doc";

  @JSONField(name = "_id")
  private String scheduleId;

}
