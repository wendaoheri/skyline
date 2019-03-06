package org.dayu.core.service;

/**
 * RuntimeConfigService
 *
 * @author Sean
 * @date 2019/2/7
 */
public interface RuntimeConfigService {

  /**
   * 根据key获取运行时配置
   *
   * @param key rt key
   */
  String getRuntimeConfig(String key);

  /**
   * 设置一个运行时配置，如果存在则更新
   *
   * @param key rt key
   * @param value rt value
   */
  void setRuntimeConfig(String key, String value);

  /**
   * 包装getRuntimeConfig，加上了类型转换
   */
  long getLong(String key);

}
