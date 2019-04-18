package org.dayu.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author sean
 */
@SpringBootApplication
@Configuration
@ComponentScan("org.dayu")
@EntityScan(basePackages = "org.dayu")
@EnableCaching
@EnableScheduling
@EnableJpaRepositories(basePackages = "org.dayu")
public class ApplicationEntry {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationEntry.class, args);
  }
}
