package org.dayu.common.data.mr;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.Data;

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

}
