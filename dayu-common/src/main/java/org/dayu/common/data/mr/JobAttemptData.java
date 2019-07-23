package org.dayu.common.data.mr;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-07-23
 */
@Data
public class JobAttemptData {

  @JSONField(name = "node_id")
  private String nodeId;

  @JSONField(name = "node_http_address")
  private String nodeHttpAddress;

  @JSONField(name = "start_time")
  private Long startTime;

  private Integer id;

  @JSONField(name = "logs_link")
  private String logsLink;

  @JSONField(name = "container_id")
  private String containerId;
}
