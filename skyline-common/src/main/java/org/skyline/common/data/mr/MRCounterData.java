package org.skyline.common.data.mr;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.Data;
import lombok.Getter;

/**
 * @author Sean Liu
 * @date 2019-07-23
 */
@Data
public class MRCounterData {

  private Map<String, Map<String, Long>> counterData = Maps.newHashMap();

  public void setCounter(String groupName, String counterName, Long value) {
    Map<String, Long> counterGroup = counterData.get(groupName);
    if (counterGroup == null) {
      counterGroup = Maps.newHashMap();
      counterData.put(groupName, counterGroup);
    }
    counterGroup.put(counterName, value);
  }

  public long getCounterValue(CounterName counterName) {
    String groupName = counterName.getCounterGroup().getGroupName();
    String name = counterName.getCounterName();
    try {
      return counterData.get(groupName).get(name);
    } catch (NullPointerException e) {
      return 0L;
    }
  }

  public long getCounterValue(String counterName) {
    CounterName cn = CounterName.valueOf(counterName);
    return getCounterValue(cn);
  }

  public enum CounterGroup {
    /**
     * org.apache.hadoop.mapreduce.FileSystemCounter
     */
    FILE_SYSTEM_COUNTER("org.apache.hadoop.mapreduce.FileSystemCounter"),
    /**
     * org.apache.hadoop.mapreduce.TaskCounter
     */
    TASK_COUNTER("org.apache.hadoop.mapreduce.TaskCounter");

    @Getter
    private String groupName;

    CounterGroup(String groupName) {
      this.groupName = groupName;
    }
  }

  public enum CounterName {
    /**
     * HDFS_BYTES_READ
     */
    HDFS_BYTES_READ(CounterGroup.FILE_SYSTEM_COUNTER, "HDFS_BYTES_READ"),

    /**
     * REDUCE_SHUFFLE_BYTES
     */
    REDUCE_SHUFFLE_BYTES(CounterGroup.TASK_COUNTER, "REDUCE_SHUFFLE_BYTES");


    @Getter
    private CounterGroup counterGroup;

    @Getter
    private String counterName;

    CounterName(CounterGroup counterGroup, String counterName) {
      this.counterGroup = counterGroup;
      this.counterName = counterName;
    }
  }
}
