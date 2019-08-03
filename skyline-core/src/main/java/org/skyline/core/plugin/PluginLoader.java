package org.skyline.core.plugin;

import lombok.extern.slf4j.Slf4j;
import org.skyline.plugin.schedule.SchedulePlugin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class PluginLoader {

  @Value("${skyline.plugin.schedule.class}")
  private String scheduleClassName;

  @Bean
  public SchedulePlugin getSchedulePlugin() {
    return loadPlugin(scheduleClassName);
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
