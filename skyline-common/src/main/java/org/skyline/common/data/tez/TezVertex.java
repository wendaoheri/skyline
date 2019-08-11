package org.skyline.common.data.tez;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import org.skyline.common.data.CounterData;

/**
 * @author Sean Liu
 * @date 2019-08-09
 */
@Data
public class TezVertex {

  public enum VertexProcessor {
    /**
     * org.apache.hadoop.hive.ql.exec.tez.MapTezProcessor
     */
    MAP("org.apache.hadoop.hive.ql.exec.tez.MapTezProcessor"),

    /**
     * org.apache.hadoop.hive.ql.exec.tez.ReduceTezProcessor
     */
    REDUCE("org.apache.hadoop.hive.ql.exec.tez.ReduceTezProcessor")
    ;
    @Getter
    private String processorClass;

    VertexProcessor(String processorClass) {
      this.processorClass = processorClass;
    }
  }

  @JSONField(name = "vertex_id")
  private String vertexId;

  @JSONField(name = "vertex_name")
  private String vertexName;

  @JSONField(name = "processor_class")
  private String processorClass;

  @JSONField(name = "in_edge_ids")
  private String[] inEdgeIds;

  @JSONField(name = "out_edge_ids")
  private String[] outEdgeIds;

  @JSONField(name = "additional_inputs")
  private List<VertexInput> additionalInputs;

  private CounterData counters;

  @JSONField(name = "num_tasks")
  private Integer numTasks;

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

  @JSONField(name = "num_killed_tasks")
  private Integer numKilledTasks;

  @JSONField(name = "init_requested_time")
  private Long initRequestedTime;

  @JSONField(name = "init_time")
  private Long initTime;

  @JSONField(name = "start_requested_time")
  private Long startRequestedTime;

  @JSONField(name = "start_time")
  private Long startTime;

  @JSONField(name = "end_time")
  private Long endTime;

  @JSONField(name = "time_taken")
  private Long timeTaken;

  private String status;

  private List<TezTask> tasks;

  @JSONField(serialize = false, name = "task_map")
  private Map<String, TezTask> taskMap;

  public void addTask(TezTask task) {
    if (this.tasks == null) {
      tasks = Lists.newArrayList();
    }
    tasks.add(task);
    if (taskMap == null) {
      taskMap = Maps.newHashMap();
    }
    taskMap.put(task.getTaskId(), task);
  }

  @Data
  public static class VertexInput {

    private String name;

    @JSONField(name = "class")
    private String clazz;

    private String initializer;
  }

  public TezTask getTaskByTaskId(String taskId) {
    if (taskMap != null) {
      return taskMap.get(taskId);
    }
    return null;
  }
}
