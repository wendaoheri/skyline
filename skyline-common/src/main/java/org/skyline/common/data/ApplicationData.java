package org.skyline.common.data;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Properties;
import org.skyline.common.data.YarnApplication.ApplicationType;
import org.skyline.common.data.YarnApplication.FinalStatus;
import org.skyline.common.data.YarnApplication.State;

/**
 * interface produced by ApplicationHandler
 *
 * @author Sean Liu
 * @date 2019-07-14
 */
public interface ApplicationData {

  String INDEX_NAME = "application_data";
  String TYPE_NAME = "_doc";

  /**
   * set YarnApplication of handlable data
   *
   * @param application yarn application
   */
  void setApplication(YarnApplication application);

  /**
   * return applicationId
   *
   * @return application Id
   */
  @JSONField(name = "application_id")
  String getApplicationId();

  /**
   * return application type
   *
   * @return applicationType
   */
  @JSONField(name = "application_type")
  ApplicationType getApplicationType();

  /**
   * return application config
   *
   * @return config of application
   */
  Properties getConf();

  /**
   * return application state
   * @return
   */
  State getState();

  /**
   * return application finalStatus
   * @return
   */
  @JSONField(name = "final_status")
  FinalStatus getFinalStatus();

}
