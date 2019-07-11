package org.dayu.common.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-04-17
 */
@Data
public class RuntimeConfig implements Model {

  public static final String INDEX_NAME = "dayu_config";

  public static final String TYPE_NAME = "_doc";

  @JSONField(name = "_id")
  private String id;

  @JSONField(name = "value")
  private String value;

}
