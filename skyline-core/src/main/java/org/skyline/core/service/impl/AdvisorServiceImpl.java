package org.skyline.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.skyline.common.data.AdvisorConfig;
import org.skyline.common.data.Records;
import org.skyline.common.data.YarnApplication.ApplicationType;
import org.skyline.core.service.AdvisorService;
import org.skyline.core.storage.IStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author Sean Liu
 * @date 2019-08-08
 */
@Slf4j
@Service
public class AdvisorServiceImpl implements AdvisorService {

  private static final String ADVISOR_CONFIG_FILE = "advisors.json";

  @Autowired
  private IStorage storage;

  /**
   * init config when app started
   */
  @PostConstruct
  private void initConfig() {
    this.getAdvisorConfigByType(ApplicationType.MAPREDUCE);
  }

  @Override
  @Cacheable(cacheNames = CACHE_NAME, key = "#p0")
  public List<AdvisorConfig> getAdvisorConfigByType(ApplicationType applicationType) {

    List<AdvisorConfig> configs = this.getAdvisorConfigFromES();
    if (CollectionUtils.isEmpty(configs)) {
      return getAdvisorConfigByTypeFromFile(applicationType);
    } else {
      log.info("Advisor config exists in Es : {}", configs);
      Map<ApplicationType, List<AdvisorConfig>> advisorMap = initConfigMap(configs);
      return advisorMap.get(applicationType);
    }

  }

  private List<AdvisorConfig> getAdvisorConfigFromES() {
    if (storage.indexExists(AdvisorConfig.INDEX_NAME)) {
      List<AdvisorConfig> configs = storage
          .findAll(AdvisorConfig.INDEX_NAME, AdvisorConfig.TYPE_NAME, AdvisorConfig.class, true);
      return configs;
    }
    return null;
  }

  private boolean advisorConfigInited() {
    return !CollectionUtils.isEmpty(getAdvisorConfigFromES());
  }

  /**
   * If we get advisor config from file, we also need to put it to es
   */
  private List<AdvisorConfig> getAdvisorConfigByTypeFromFile(ApplicationType applicationType) {

    try {
      String path = this.getClass().getClassLoader().getResource(ADVISOR_CONFIG_FILE).getPath();
      log.info("Read advisor config from file : {}", path);
      JSONArray ja = JSON.parseObject(new FileInputStream(new File(path)), JSONArray.class);
      List<AdvisorConfig> configs = Lists.newArrayList();
      for (int i = 0; i < ja.size(); i++) {
        AdvisorConfig advisorConfig = ja.getObject(i, AdvisorConfig.class);
        configs.add(advisorConfig);
      }

      Map<ApplicationType, List<AdvisorConfig>> advisorMap = initConfigMap(configs);
      // Save advisors to es
      try {
        if (!advisorConfigInited()) {
          storage
              .bulkUpsert(AdvisorConfig.INDEX_NAME, AdvisorConfig.TYPE_NAME,
                  Records.fromObject(configs));
          log.info("Update advisor configs to es success");
        } else {
          log.info("Advisor config inited, skip update");
        }
      } catch (ExecutionException | InterruptedException e) {
        log.error("Save advisor config error", e);
      }

      log.info("Loaded advisor configs : {}", advisorMap);
      return advisorMap.get(applicationType);
    } catch (IOException e) {
      log.error("Init advisors error ", e);
    }
    return null;
  }


  private Map<ApplicationType, List<AdvisorConfig>> initConfigMap(
      Collection<AdvisorConfig> configs) {
    Map<ApplicationType, List<AdvisorConfig>> advisorMap = Maps.newHashMap();
    for (AdvisorConfig ac : configs) {
      ApplicationType applicationType = ac.getApplicationType();

      List<AdvisorConfig> typeConfigs = advisorMap.get(applicationType);
      if (typeConfigs == null) {
        typeConfigs = Lists.newArrayList();
        advisorMap.put(applicationType, typeConfigs);
      }
      typeConfigs.add(ac);
    }
    return advisorMap;
  }

}
