package org.dayu.common.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-04-17
 */
@Data
public class YarnApplication implements Model{

  public static final String DATABASE_NAME = "yarn_application";
  public static final String TABLE_NAME = "yarn_application";

  @JSONField(name = "_id")
  private String id;

  @JSONField(name = "application_id")
  private String applicationId;

  private String user;

  private String name;

  private String queue;

  private String state;

  @JSONField(name = "final_status")
  private String finalStatus;

  private int progress;

  @JSONField(name = "tracking_ui")
  private String trackingUI;

  @JSONField(name = "tracking_url")
  private String trackingUrl;

  private String diagnostics;

  @JSONField(name = "cluster_id")
  private long clusterId;

  @JSONField(name = "application_type")
  private String applicationType;

  @JSONField(name = "application_tags")
  private String applicationTags;

  @JSONField(name = "started_time")
  private long startedTime;

  @JSONField(name = "finished_time")
  private long finishedTime;

  @JSONField(name = "elapsed_time")
  private int elapsedTime;

  @JSONField(name = "am_container_logs")
  private String amContainerLogs;

  @JSONField(name = "am_host_http_address")
  private String amHostHttpAddress;

  @JSONField(name = "allocated_mb")
  private long allocatedMB;

  @JSONField(name = "allocated_vcores")
  private long allocatedVCores;

  @JSONField(name = "running_containers")
  private int runningContainers;

  @JSONField(name = "memory_seconds")
  private long memorySeconds;

  @JSONField(name = "vcore_seconds")
  private long vcoreSeconds;

  @JSONField(name = "preempted_resource_mb")
  private long preemptedResourceMB;

  @JSONField(name = "preempted_resource_vcores")
  private long preemptedResourceVCores;

  @JSONField(name = "num_non_am_container_preempted")
  private int numNonAMContainerPreempted;

  @JSONField(name = "num_am_container_preempted")
  private int numAMContainerPreempted;

  @JSONField(name = "log_aggregation_status")
  private String logAggregationStatus;

  @JSONField(name = "schedule_id")
  private String scheduleId;

  @JSONField(name = "new_schedule")
  private Integer newSchedule;

  @JSONField(name = "dt")
  private String dt;

}
