package org.dayu.core.handler.advisor.mr;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.HandlerResultDetail;
import org.dayu.common.data.mr.MRApplicationData;
import org.dayu.common.data.mr.MRTaskData;
import org.dayu.common.data.mr.MRTaskData.MRTaskType;
import org.dayu.common.model.YarnApplication.ApplicationType;
import org.dayu.core.handler.advisor.MultiAbstractAdvisor;
import org.springframework.stereotype.Component;

/**
 * This advisor calculate skewness of map or reduce tasks, include - execute time - file bytes read
 * - file bytes write
 *
 * @author Sean Liu
 * @date 2019-07-30
 */
@Component
@Slf4j
public class MRSkewAdvisor extends MultiAbstractAdvisor {

  @Override
  public List<HandlerResultDetail> multiAdvise(ApplicationData applicationData) {
    MRApplicationData mrData = (MRApplicationData) applicationData;
    for (MRTaskType type : MRTaskType.values()) {
      List<MRTaskData> taskDataList = mrData.getTaskByType(type);

    }
    return null;
  }

  private void calSkewness(List<MRTaskData> taskDataList) {

    for (MRTaskData taskData : taskDataList) {
      taskData.getElapsedTime();

    }

  }

  @Override
  public int getOrder() {
    return 0;
  }

  @Override
  public ApplicationType getApplicationType() {
    return ApplicationType.MAPREDUCE;
  }
}
