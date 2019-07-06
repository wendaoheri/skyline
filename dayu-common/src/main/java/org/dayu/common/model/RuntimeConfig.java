package org.dayu.common.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-04-17
 */
@Data
public class RuntimeConfig implements Model {

  public static final String DATABASE_NAME = "runtime_config";

  public static final String TABLE_NAME = "runtime_config";

  @JSONField(name = "_id")
  private String id;

  @JSONField(name = "value")
  private String value;

}
