package org.dayu.common.data;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-07-22
 */
@Data
public class YarnMetrics implements Model {

  public static final String DATABASE_NAME = "yarn_metrics";
  public static final String TABLE_NAME = "_doc";

  @JSONField(name="apps_submitted")
  private Integer appsSubmitted;

  @JSONField(name="apps_completed")
  private Integer appsCompleted;

  @JSONField(name="apps_pending")
  private Integer appsPending;

  @JSONField(name="apps_running")
  private Integer appsRunning;

  @JSONField(name="apps_failed")
  private Integer appsFailed;

  @JSONField(name="apps_killed")
  private Integer appsKilled;

  @JSONField(name="reserved_mb")
  private Integer reservedMB;

  @JSONField(name="available_mb")
  private Integer availableMB;

  @JSONField(name="allocated_mb")
  private Integer allocatedMB;

  @JSONField(name="reserved_virtual_cores")
  private Integer reservedVirtualCores;

  @JSONField(name="available_virtual_cores")
  private Integer availableVirtualCores;

  @JSONField(name="allocated_virtual_cores")
  private Integer allocatedVirtualCores;

  @JSONField(name="containers_allocated")
  private Integer containersAllocated;

  @JSONField(name="containers_reserved")
  private Integer containersReserved;

  @JSONField(name="containers_pending")
  private Integer containersPending;

  @JSONField(name="total_mb")
  private Integer totalMB;

  @JSONField(name="total_virtual_cores")
  private Integer totalVirtualCores;

  @JSONField(name="total_nodes")
  private Integer totalNodes;

  @JSONField(name="lost_nodes")
  private Integer lostNodes;

  @JSONField(name="unhealthy_nodes")
  private Integer unhealthyNodes;

  @JSONField(name="decommissioned_nodes")
  private Integer decommissionedNodes;

  @JSONField(name="rebooted_nodes")
  private Integer rebootedNodes;

  @JSONField(name="active_nodes")
  private Integer activeNodes;

}
