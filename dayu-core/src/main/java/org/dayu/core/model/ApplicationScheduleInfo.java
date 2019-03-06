package org.dayu.core.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "application_schedule_info")
public class ApplicationScheduleInfo {

  @Data
  @NoArgsConstructor
  @Embeddable
  public static class ApplicationScheduleId implements Serializable {

    @Column(length = 50)
    private String scheduleId;

    @Column(length = 50)
    private String applicationId;

  }

  @EmbeddedId
  private ApplicationScheduleId applicationScheduleId;

}
