package org.skyline.common.data.tez;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import org.skyline.common.data.tez.TezVertex.VertexProcessor;

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

  /**
   * type is enum value of #VertexProcessor#
   * @param type
   * @return
   */
  public List<TezVertex> getVertices(String type) {
    String processorClass = VertexProcessor.valueOf(type).getProcessorClass();
    return vertices.values().stream()
        .filter(x -> x.getProcessorClass().equalsIgnoreCase(processorClass))
        .collect(Collectors.toList());
  }

}
