package org.dayu.common.data.mr;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-07-23
 */
@Data
public class MRTaskData {

  public enum MRTaskType {
    MAP, REDUCE
  }

  public enum MRTaskState {

  }

  @JSONField(name = "task_attempt_data_list")
  private List<TaskAttemptData> taskAttemptDataList;

  @JSONField(name = "task_counter_data")
  private MRCounterData taskCounterData;

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
