package org.skyline.common.data.tez;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Map;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-08-09
 */
@Data
public class TezDAG {

  @JSONField(name = "dag_id")
  private String dagId;

  @JSONField(name = "dag_name")
  private String dagName;

  @JSONField(name = "start_time")
  private Long startTime;

  @JSONField(name = "init_time")
  private Long initTime;

  @JSONField(name = "end_time")
  private Long endTime;

  private String status;

  private Map<String, TezEdge> edges;

  private Map<String, TezVertex> vertices;

}
