package org.skyline.common.data.tez;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import org.skyline.common.data.CounterData;
import org.skyline.common.data.tez.TezVertex.VertexProcessor;

/**
 * @author Sean Liu
 * @date 2019-08-09
 */
@Data
public class TezDAG {

  @JSONField(name = "application_id")
  private String applicationId;

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

  @JSONField(name = "time_taken")
  private Long timeTaken;

  @JSONField(name = "num_failed_task_attempts")
  private Integer numFailedTaskAttempts;

  @JSONField(name = "num_killed_task_attempts")
  private Integer numKilledTaskAttempts;

  @JSONField(name = "num_completed_tasks")
  private Integer numCompletedTasks;

  @JSONField(name = "num_succeeded_tasks")
  private Integer numSucceededTasks;

  @JSONField(name = "num_failed_tasks")
  private Integer numFailedTasks;

  @JSONField(name = "num_failed_task_attempts")
  private Integer numKilledTasks;

  private String status;

  private CounterData counters;

  private Map<String, TezEdge> edges;

  private Map<String, TezVertex> vertices;

  /**
   * type is enum value of #VertexProcessor#
   */
  public List<TezVertex> getVertices(String type) {
    String processorClass = VertexProcessor.valueOf(type).getProcessorClass();
    return vertices.values().stream()
        .filter(x -> x.getProcessorClass().equalsIgnoreCase(processorClass))
        .collect(Collectors.toList());
  }

}
