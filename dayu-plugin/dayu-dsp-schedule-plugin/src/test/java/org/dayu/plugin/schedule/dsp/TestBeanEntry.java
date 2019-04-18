package org.dayu.plugin.schedule.dsp;

import org.dayu.plugin.schedule.SchedulePlugin;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * TestBeanEntry
 *
 * @author Sean
 * @date 2019/2/4
 */
@EnableAutoConfiguration
@ComponentScan({"org.dayu"})
public class TestBeanEntry {

  @Bean
  public SchedulePlugin loadPlugin(){
    return new DSPSchedulePlugin();
  }

}
