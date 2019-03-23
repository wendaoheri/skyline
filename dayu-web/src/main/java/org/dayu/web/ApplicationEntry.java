package org.dayu.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author sean
 */
@SpringBootApplication
@Configuration
@ComponentScan("org.dayu")
@EnableJpaRepositories("org.dayu")
@EntityScan(basePackages = "org.dayu")
public class ApplicationEntry {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationEntry.class, args);
  }
}
