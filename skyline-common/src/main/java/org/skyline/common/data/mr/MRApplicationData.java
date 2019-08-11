package org.skyline.common.data.mr;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import lombok.Data;
import org.skyline.common.data.ApplicationData;
import org.skyline.common.data.CounterData;
import org.skyline.common.data.YarnApplication;
import org.skyline.common.data.YarnApplication.ApplicationType;
import org.skyline.common.data.YarnApplication.FinalStatus;
import org.skyline.common.data.YarnApplication.State;
import org.skyline.common.data.mr.MRTask.MRTaskType;

/**
 * @author Sean Liu
 * @date 2019-07-14
 */
@Data
public class MRApplicationData implements ApplicationData {

  @JSONField(name = "_id")
  private String id;

  @JSONField(serialize = false)
  private transient YarnApplication application;

  @JSONField(name = "job")
  private Job job;

  private Properties conf;

  @JSONField(name = "counters")
  private CounterData counters;

  @JSONField(name = "tasks")
  private List<MRTask> tasks;


  @Override
  public String getApplicationId() {
    return application.getId();
  }

  @Override
  public ApplicationType getApplicationType() {
    return ApplicationType.MAPREDUCE;
  }

  @Override
  public State getState() {
    if (application.getState() == null) {
      return null;
    }
    return State.valueOf(application.getState());
  }

  @Override
  public FinalStatus getFinalStatus() {
    if (application.getFinalStatus() == null) {
      return null;
    }
    return FinalStatus.valueOf(application.getFinalStatus());
  }

  public List<MRTask> getTaskByType(MRTaskType type) {
    return tasks.parallelStream().filter(x -> type.equals(x.getType()))
        .collect(Collectors.toList());
  }

  public List<MRTask> getTaskByType(String typeName) {
    MRTaskType type = MRTaskType.valueOf(typeName);
    return getTaskByType(type);
  }

}
