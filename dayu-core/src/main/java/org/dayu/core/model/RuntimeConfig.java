package org.dayu.core.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * RuntimeConfig
 *
 * @author Sean
 * @date 2019/2/5
 */
@Data
@Entity
@Table(name = "runtime_config")
public class RuntimeConfig implements Serializable {

  @Id
  @Column(length = 50, nullable = false, unique = true, name = "runtime_key")
  private String runtimeKey;

  @Column(nullable = false, unique = true, name = "runtime_value")
  private String runtimeValue;

//  @Column(name = "create_time", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
//  private Date createTime;

  @Column(name = "update_time", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  private Date updateTime;
}
