package org.skyline.core.service;

import java.util.List;
import org.skyline.common.data.AdvisorConfig;
import org.skyline.common.data.YarnApplication.ApplicationType;

/**
 * @author Sean Liu
 * @date 2019-08-08
 */
public interface AdvisorService {

  String CACHE_NAME = "advisor_config";

  /**
   * Get advisors from file or es, this method provides cache feature of get config,
   *
   * If config not exists in ES or ES is down, read from file and put it to ES
   *
   */
  List<AdvisorConfig> getAdvisorConfigByType(ApplicationType applicationType);

}
