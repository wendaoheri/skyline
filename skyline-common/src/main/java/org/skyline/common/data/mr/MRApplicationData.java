package org.skyline.common.data.mr;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import lombok.Data;
import org.skyline.common.data.ApplicationData;
import org.skyline.common.data.YarnApplication;
import org.skyline.common.data.YarnApplication.ApplicationType;
import org.skyline.common.data.YarnApplication.FinalStatus;
import org.skyline.common.data.YarnApplication.State;
import org.skyline.common.data.mr.MRTaskData.MRTaskType;

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

  @JSONField(name = "job_data")
  private JobData jobData;

  private Properties conf;

  @JSONField(name = "job_counter_data")
  private MRCounterData jobCounterData;

  @JSONField(name = "task_data_list")
  private List<MRTaskData> taskDataList;

  @JSONField(name = "job_attempt_data_list")
  private List<JobAttemptData> jobAttemptDataList;


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
    return State.valueOf(application.getState());
  }

  @Override
  public FinalStatus getFinalStatus() {
    return FinalStatus.valueOf(application.getFinalStatus());
  }

  public List<MRTaskData> getTaskByType(MRTaskType type) {
    return taskDataList.parallelStream().filter(x -> type.equals(x.getType()))
        .collect(Collectors.toList());
  }

  public List<MRTaskData> getTaskByType(String typeName) {
    MRTaskType type = MRTaskType.valueOf(typeName);
    return getTaskByType(type);
  }

}
