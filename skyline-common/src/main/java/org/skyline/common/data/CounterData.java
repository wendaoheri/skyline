package org.skyline.common.data;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Maps;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sean Liu
 * @date 2019-07-23
 */
@Data
@Slf4j
public class CounterData {

  public static final CounterData EMPTY_COUNTER = new CounterData();

  @JSONField(name = "counter_data")
  private Map<String, Map<String, Long>> counterData = Maps.newHashMap();

  public void setCounter(String groupName, String counterName, Long value) {
    Map<String, Long> counterGroup = counterData.get(groupName);
    if (counterGroup == null) {
      counterGroup = Maps.newHashMap();
      counterData.put(groupName, counterGroup);
    }
    counterGroup.put(counterName, value);
  }

  public Long getCounterValue(String groupName, String counterName) {
    try {
      return counterData.get(groupName).get(counterName);
    } catch (NullPointerException e) {
      log.warn("No counter for [group | name] [{} | {}]", groupName, counterName);
      return null;
    }
  }

}
