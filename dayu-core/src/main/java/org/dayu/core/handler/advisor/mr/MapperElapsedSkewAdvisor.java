package org.dayu.core.handler.advisor.mr;

import java.util.List;
import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.DisplayMessage;
import org.dayu.common.data.ResultDetail;
import org.dayu.common.data.mr.MRApplicationData;
import org.dayu.common.data.mr.MRTaskData;
import org.dayu.common.data.mr.MRTaskData.MRTaskType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-31
 */
@Component
@ConfigurationProperties("org.dayu.core.handler.advisor.mr.mapper-elapsed-skew-advisor")
public class MapperElapsedSkewAdvisor extends AbstractMRSkewAdvisor {

  @Override
  protected long[] getData(ApplicationData applicationData) {
    MRApplicationData mrData = (MRApplicationData) applicationData;
    List<MRTaskData> mapper = mrData.getTaskByType(MRTaskType.MAP);
    return mapper.parallelStream().mapToLong(x -> x.getElapsedTime()).toArray();
  }


  @Override
  public DisplayMessage display(ResultDetail detail) {
    return null;
  }
}
