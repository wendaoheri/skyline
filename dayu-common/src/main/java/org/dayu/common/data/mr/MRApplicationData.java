package org.dayu.common.data.mr;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import lombok.Data;
import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.mr.MRTaskData.MRTaskType;
import org.dayu.common.model.YarnApplication;
import org.dayu.common.model.YarnApplication.ApplicationType;

/**
 * @author Sean Liu
 * @date 2019-07-14
 */
@Data
public class MRApplicationData implements ApplicationData {

  private YarnApplication application;

  private JobData jobData;

  private Properties conf;

  private MRCounterData jobCounterData;

  private List<MRTaskData> taskDataList;

  private List<JobAttemptData> jobAttemptDataList;


  @Override
  public String getApplicationId() {
    return application.getId();
  }

  @Override
  public ApplicationType getApplicationType() {
    return ApplicationType.MAPREDUCE;
  }

  public List<MRTaskData> getTaskByType(MRTaskType type) {
    return taskDataList.parallelStream().filter(x -> type.equals(x.getType()))
        .collect(Collectors.toList());
  }

}
