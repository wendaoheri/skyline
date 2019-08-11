package org.skyline.common.data.tez;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-08-10
 */
@Data
public class TezEdge {

  @JSONField(name = "edge_id")
  private String edgeId;

  @JSONField(name = "data_movement_type")
  private String dataMovementType;

  @JSONField(name = "data_source_type")
  private String dataSourceType;

  @JSONField(name = "scheduling_type")
  private String schedulingType;

  @JSONField(name = "edge_source_class")
  private String edgeSourceClass;

  @JSONField(name = "edge_destination_class")
  private String edgeDestinationClass;
}
