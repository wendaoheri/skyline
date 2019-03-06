package org.dayu.core.service.impl;

import org.dayu.core.model.RuntimeConfig;
import org.dayu.core.repository.RuntimeConfigRepository;
import org.dayu.core.service.RuntimeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RuntimeConfigServiceImpl
 *
 * @author Sean
 * @date 2019/2/7
 */
@Service
public class RuntimeConfigServiceImpl implements RuntimeConfigService {

  @Autowired
  private RuntimeConfigRepository runtimeConfigRepository;


  @Override
  public String getRuntimeConfig(String key) {
    RuntimeConfig rc = runtimeConfigRepository
        .findByRuntimeKey(key);
    if (null != rc) {
      return rc.getRuntimeValue();
    }
    return null;
  }

  @Override
  public void setRuntimeConfig(String key, String value) {
    RuntimeConfig config = new RuntimeConfig();
    config.setRuntimeKey(key);
    config.setRuntimeValue(value);
    runtimeConfigRepository.save(config);
  }

  @Override
  public long getLong(String key) {
    String value = this.getRuntimeConfig(key);
    if (value != null) {
      return Long.valueOf(value);
    }
    return 0;
  }
}