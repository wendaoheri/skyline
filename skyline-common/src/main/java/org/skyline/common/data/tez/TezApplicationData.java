package org.skyline.common.data.tez;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Properties;
import lombok.Data;
import org.skyline.common.data.ApplicationData;
import org.skyline.common.data.YarnApplication;
import org.skyline.common.data.YarnApplication.ApplicationType;
import org.skyline.common.data.YarnApplication.FinalStatus;
import org.skyline.common.data.YarnApplication.State;

/**
 * @author Sean Liu
 * @date 2019-08-09
 */
@Data
public class TezApplicationData implements ApplicationData {

  private YarnApplication application;

  private Properties conf;

  @JSONField(name = "tez_dag")
  private TezDAG tezDAG;

  @Override
  public void setApplication(YarnApplication application) {
    this.application = application;
  }

  @Override
  public String getApplicationId() {
    return application.getApplicationId();
  }

  @Override
  public ApplicationType getApplicationType() {
    return ApplicationType.TEZ;
  }

  @Override
  public Properties getConf() {
    return this.conf;
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
}
