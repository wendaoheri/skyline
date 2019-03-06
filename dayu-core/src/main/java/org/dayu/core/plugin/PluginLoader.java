package org.dayu.core.plugin;

import lombok.extern.slf4j.Slf4j;
import org.dayu.plugin.schedule.SchedulePlugin;
import org.dayu.plugin.storage.TraceStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class PluginLoader {

  @Value("${dayu.plugin.schedule.class}")
  private String scheduleClassName;

  @Value("${dayu.plugin.storage.class}")
  private String traceStorageClassName;

  @Bean
  public SchedulePlugin getSchedulePlugin() {
    return loadPlugin(scheduleClassName);
  }

  @Bean
  public TraceStorage getTraceStorage() {
    return loadPlugin(traceStorageClassName);
  }

  private <T> T loadPlugin(String className) {
    T obj = null;
    try {
      obj = (T) Class.forName(className).newInstance();
      log.info("Loaded plugin {}", className);
    } catch (ClassNotFoundException e) {
      log.error("Plugin class not found : {}", className);
      log.error(e.getMessage());
    } catch (IllegalAccessException e) {
      log.error(e.getMessage());
    } catch (InstantiationException e) {
      log.error(e.getMessage());
    }
    return obj;
  }

}
