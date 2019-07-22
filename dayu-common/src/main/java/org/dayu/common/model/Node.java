package org.dayu.common.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-07-22
 */
@Data
public class Node implements Model {

  public static final String DATABASE_NAME = "nodes";
  public static final String TABLE_NAME = "nodes";

  private String rack;

  private String state;

  private String id;

  @JSONField(name = "node_host_name")
  private String nodeHostName;

  @JSONField(name = "node_http_address")
  private String nodeHTTPAddress;

  @JSONField(name = "health_status")
  private String healthStatus;

  @JSONField(name = "last_health_update")
  private Long lastHealthUpdate;

  @JSONField(name = "health_report")
  private String healthReport;

  @JSONField(name = "num_containers")
  private Integer numContainers;

  @JSONField(name = "used_memory_mb")
  private Integer usedMemoryMB;

  @JSONField(name = "avail_memory_mb")
  private Integer availMemoryMB;

  @JSONField(name = "used_virtual_cores")
  private Integer usedVirtualCores;

  @JSONField(name = "available_virtual_cores")
  private Integer availableVirtualCores;

}
