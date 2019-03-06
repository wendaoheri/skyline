package org.dayu.core.plugin;

import lombok.extern.slf4j.Slf4j;
import org.dayu.plugin.schedule.SchedulePlugin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class PluginLoader {

  @Value("${dayu.plugin.schedule.class}")
  private String scheduleName;

  @Bean
  public SchedulePlugin getSchedulePlugin() {
    SchedulePlugin schedulePlugin = null;
    try {
      Class<SchedulePlugin> scheduleClass = (Class<SchedulePlugin>) Class.forName(scheduleName);
      schedulePlugin = scheduleClass.newInstance();
      log.info("Loaded schedule plugin {}", scheduleName);
    } catch (ClassNotFoundException e) {
      log.error("Schedule plugin class not found : {}, please check {dayu.plugin.scheduler} config",
          scheduleName);
      log.error(e.getMessage());
    } catch (IllegalAccessException e) {
      log.error(e.getMessage());
    } catch (InstantiationException e) {
      log.error(e.getMessage());
    }
    return schedulePlugin;
  }

}
