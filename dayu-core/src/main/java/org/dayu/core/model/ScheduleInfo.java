package org.dayu.core.model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-03-26
 */

@Data
@Entity
@Table(name = "schedule_info", indexes = {@Index(columnList = "scheduleId")})
public class ScheduleInfo implements Serializable {

  @Id
  @Column(length = 50)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 50, unique = true)

  private String scheduleId;

//  @OneToMany(mappedBy = "scheduleInfo", fetch = FetchType.LAZY)
//  private Set<YarnApplication> applications;

}
