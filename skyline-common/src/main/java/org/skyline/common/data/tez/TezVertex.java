package org.skyline.common.data.tez;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-08-09
 */
@Data
public class TezVertex {

  private String vertexId;

  private long startTime;

  private long finishTime;

  private long elapsed;

  @JSONField(name="num_tasks")
  private int numTasks;

  @JSONField(name="num_failed_task_attempts")
  private int numFailedTaskAttempts;

  @JSONField(name="processor_class_name")
  private String processorClassName;

  @JSONField(name="num_killed_task_attempts")
  private int numKilledTaskAttempts;

  @JSONField(name="vertex_name")
  private String vertexName;
}
