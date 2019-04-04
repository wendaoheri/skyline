package org.dayu.core.model;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

/**
 * @author Sean Liu
 */
@Data
@Entity
@Table(name = "yarn_application")
public class YarnApplication implements Serializable {

  @Id
  @Column(length = 50, unique = true, nullable = false)
  private String id;

  @Column(length = 100)
  private String user;

  @Column(length = 100)
  private String name;

  @Column(length = 100)
  private String queue;

  @Column(length = 20)
  private String state;

  @Column(length = 20, name = "final_status")
  @JSONField(name = "final_status")
  private String finalStatus;

  @Column
  private int progress;

  @Column(length = 20, name = "tracking_ui")
  @JSONField(name = "tracking_ui")
  private String trackingUI;

  @Column(name = "tracking_url")
  @JSONField(name = "tracking_url")
  private String trackingUrl;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Column(columnDefinition = "TEXT")
  private String diagnostics;

  @Column(name = "cluster_id")
  @JSONField(name = "cluster_id")
  private long clusterId;

  @Column(length = 20, name = "application_type")
  @JSONField(name = "application_type")
  private String applicationType;

  @Column(length = 100, name = "application_tags")
  @JSONField(name = "application_tags")
  private String applicationTags;

  @Column(name = "started_time")
  @JSONField(name = "started_time")
  private long startedTime;

  @Column(name = "finished_time")
  @JSONField(name = "finished_time")
  private long finishedTime;

  @Column(name = "elapsed_time")
  @JSONField(name = "elapsed_time")
  private int elapsedTime;

  @Column(length = 100, name = "am_container_logs")
  @JSONField(name = "am_container_logs")
  private String amContainerLogs;

  @Column(length = 100, name = "am_host_http_address")
  @JSONField(name = "am_host_http_address")
  private String amHostHttpAddress;

  @Column(name = "allocated_mb")
  @JSONField(name = "allocated_mb")
  private long allocatedMB;

  @Column(name = "allocated_vcores")
  @JSONField(name = "allocated_vcores")
  private long allocatedVCores;

  @Column(name = "running_containers")
  @JSONField(name = "running_containers")
  private int runningContainers;

  @Column(name = "memory_seconds")
  @JSONField(name = "memory_seconds")
  private long memorySeconds;

  @Column(name = "vcore_seconds")
  @JSONField(name = "vcore_seconds")
  private long vcoreSeconds;

  @Column(name = "preempted_resource_mb")
  @JSONField(name = "preempted_resource_mb")
  private long preemptedResourceMB;

  @Column(name = "preempted_resource_vcores")
  @JSONField(name = "preempted_resource_vcores")
  private long preemptedResourceVCores;

  @Column(name = "num_non_am_container_preempted")
  @JSONField(name = "num_non_am_container_preempted")
  private int numNonAMContainerPreempted;

  @Column(name = "num_am_container_preempted")
  @JSONField(name = "num_am_container_preempted")
  private int numAMContainerPreempted;

  @Column(length = 100, name = "log_aggregation_status")
  @JSONField(name = "log_aggregation_status")
  private String logAggregationStatus;

  @Column(name = "schedule_id")
  @JSONField(name = "schedule_id")
  private String scheduleId;

//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "scheduleId", referencedColumnName = "scheduleId", updatable = false, insertable = false)
//  private ScheduleInfo scheduleInfo;
}
