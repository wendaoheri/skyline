package org.skyline.common.data.mr;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.skyline.common.data.mr.MRTaskData.MRTaskType;

/**
 * @author Sean Liu
 * @date 2019-07-23
 */
@Data
public class TaskAttemptData {

  @JSONField(name = "assigned_container_id")
  private String assignedContainerId;

  private String progress;

  @JSONField(name = "elapsed_time")
  private String elapsedTime;

  private String state;

  private String diagnostics;

  private String rack;

  @JSONField(name = "node_http_address")
  private String nodeHttpAddress;

  @JSONField(name = "start_time")
  private String startTime;

  private String id;

  private MRTaskType type;

  @JSONField(name = "finish_time")
  private String finishTime;
}
