package org.dayu.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author sean
 */
@SpringBootApplication
@Configuration
@ComponentScan("org.dayu")
@EnableCaching
@EnableScheduling
@EnableJms
public class ApplicationEntry {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationEntry.class, args);
  }
}