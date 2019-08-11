package org.skyline.common.data.tez;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-08-10
 */
@Data
public class TezTaskAttempt {

  @JSONField(name = "attempt_id")
  private String attemptId;

  @JSONField(name = "creation_time")
  private Long creationTime;

  @JSONField(name = "time_taken")
  private Long timeTaken;

  @JSONField(name = "start_time")
  private Long startTime;

  @JSONField(name = "allocation_time")
  private Long allocationTime;

  @JSONField(name = "end_time")
  private Long endTime;

  @JSONField(name = "node_http_address")
  private String nodeHttpAddress;

  @JSONField(name = "container_id")
  private String containerId;

  @JSONField(name = "node_id")
  private String nodeId;

  private String status;
}
