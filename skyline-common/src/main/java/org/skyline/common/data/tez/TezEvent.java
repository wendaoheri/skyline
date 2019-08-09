package org.skyline.common.data.tez;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-08-09
 */
@Data
public class TezEvent {

  private long timestamp;

  @JSONField(name = "eventtype")
  private String eventType;
}
