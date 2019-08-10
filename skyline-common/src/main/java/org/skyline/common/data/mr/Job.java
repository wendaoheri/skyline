package org.skyline.common.data.mr;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-07-23
 */
@Data
public class Job {

  public enum MRJobState {
    NEW, INITED, RUNNING, SUCCEEDED, FAILED, KILL_WAIT, KILLED, ERROR
  }

  @JSONField(name = "submit_time")
  private Long submitTime;

  @JSONField(name = "avg_reduce_time")
  private Long avgReduceTime;

  @JSONField(name = "failed_reduce_attempts")
  private Integer failedReduceAttempts;

  private MRJobState state;

  @JSONField(name = "successful_reduce_attempts")
  private Integer successfulReduceAttempts;

  private String user;

  @JSONField(name = "reduces_total")
  private Integer reducesTotal;

  @JSONField(name = "maps_completed")
  private Integer mapsCompleted;

  @JSONField(name = "start_time")
  private Integer startTime;

  private String id;

  @JSONField(name = "avg_map_time")
  private Long avgMapTime;

  @JSONField(name = "successful_map_attempts")
  private Integer successfulMapAttempts;

  private String name;

  @JSONField(name = "avg_shuffle_time")
  private Integer avgShuffleTime;

  @JSONField(name = "reduces_completed")
  private Integer reducesCompleted;

  private String diagnostics;

  @JSONField(name = "failed_map_attempts")
  private Integer failedMapAttempts;

  @JSONField(name = "avg_merge_time")
  private Long avgMergeTime;

  @JSONField(name = "killed_reduce_attempts")
  private Integer killedReduceAttempts;

  @JSONField(name = "maps_total")
  private Integer mapsTotal;

  private String queue;

  private Boolean uberized;

  @JSONField(name = "killed_map_attempts")
  private Integer killedMapAttempts;

  @JSONField(name = "finish_time")
  private Long finishTime;
}
