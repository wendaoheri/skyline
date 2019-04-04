package org.dayu.plugin.schedule.dsp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-04-02
 */
@Data
@Entity
@Table(name = "job_application_log")
public class JobApplicationLog {

  @Id
  String rowid;

  @Column(name = "job_id")
  private Long jobId;

  @Column(name = "hive_id")
  private Long hiveId;

  @Column(name = "job_log_id")
  private Long jobLogId;

  @Column(name = "job_frequency")
  private Long jobFrequency;

  @Column(name = "application_log")
  private String applicationLog;

}
