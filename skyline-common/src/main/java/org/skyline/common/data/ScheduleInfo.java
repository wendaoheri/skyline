package org.skyline.common.data;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-04-17
 */
@Data
public class ScheduleInfo {

  public static final String INDEX_NAME = "schedule_info";

  public static final String TYPE_NAME = "_doc";

  @JSONField(name = "_id")
  private String scheduleId;

}
