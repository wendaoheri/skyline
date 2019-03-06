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
  private String scheduleName;

  @Value("${dayu.plugin.storage.class}")
  private String traceStorage;

  @Bean
  public SchedulePlugin getSchedulePlugin() {
    return loadPlugin(scheduleName);
  }

  @Bean
  public TraceStorage getTraceStorage() {
    return loadPlugin(traceStorage);
  }

  private <T> T loadPlugin(String className) {
    T obj = null;
    try {
      obj = (T) Class.forName(className).newInstance();
      log.info("Loaded schedule plugin {}", scheduleName);
    } catch (ClassNotFoundException e) {
      log.error("Schedule plugin class not found : {}",
          scheduleName);
      log.error(e.getMessage());
    } catch (IllegalAccessException e) {
      log.error(e.getMessage());
    } catch (InstantiationException e) {
      log.error(e.getMessage());
    }
    return obj;
  }

}
