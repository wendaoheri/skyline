package org.dayu.plugin.schedule.dsp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Sean Liu
 * @date 2019-04-02
 */
//@SpringBootApplication
//@Slf4j
//@EnableCaching
//@EnableScheduling
//@Configuration
//@ComponentScan("org.dayu")
//@EntityScan(basePackages = "org.dayu")
public class ApplicationTest {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationTest.class, args);
  }
}
