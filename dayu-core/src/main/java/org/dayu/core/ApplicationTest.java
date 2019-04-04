package org.dayu.core;

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
//@Configuration
//@ComponentScan("org.dayu")
//@EntityScan(basePackages = "org.dayu")
//@EnableCaching
//@EnableScheduling
public class ApplicationTest {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationTest.class, args);
  }
}
