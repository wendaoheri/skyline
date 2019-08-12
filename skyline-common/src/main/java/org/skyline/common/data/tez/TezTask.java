package org.skyline.common.data.tez;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import java.util.List;
import lombok.Data;
import org.skyline.common.data.CounterData;

/**
 * @author Sean Liu
 * @date 2019-08-10
 */
@Data
public class TezTask {

  @JSONField(name = "task_id")
  private String taskId;

  private CounterData counters;

  @JSONField(name = "num_failed_task_attempts")
  private Integer numFailedTaskAttempts;

  @JSONField(name = "time_taken")
  private Long timeTaken;

  @JSONField(name = "scheduled_time")
  private Long scheduledTime;

  @JSONField(name = "start_time")
  private Long startTime;

  @JSONField(name = "end_time")
  private Long endTime;

  @JSONField(name = "status")
  private String status;

  @JSONField(name = "successful_attempt_id")
  private String successfulAttemptId;

  private List<TezTaskAttempt> attempts;

  public void addAttempt(TezTaskAttempt attempt) {
    if (this.attempts == null) {
      attempts = Lists.newArrayList();
    }
    attempts.add(attempt);
  }

}
