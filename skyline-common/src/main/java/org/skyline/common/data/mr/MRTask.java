package org.skyline.common.data.mr;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import lombok.Data;
import org.skyline.common.data.CounterData;

/**
 * @author Sean Liu
 * @date 2019-07-23
 */
@Data
public class MRTask {

  public enum MRTaskType {
    MAP, REDUCE
  }

  public enum MRTaskState {

  }

  @JSONField(name = "task_attempts")
  private List<TaskAttempt> taskAttempts;

  private CounterData counters;

  private Integer progress;

  @JSONField(name = "elapsed_time")
  private Long elapsedTime;

  private MRTaskState state;

  @JSONField(name = "start_time")
  private Long startTime;

  private String id;

  private MRTaskType type;

  @JSONField(name = "successful_attempt")
  private String successfulAttempt;

  @JSONField(name = "finish_time")
  private Long finishTime;

}
