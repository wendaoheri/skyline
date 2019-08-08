package org.skyline.web;

import static org.skyline.core.service.AdvisorService.CACHE_NAME;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.skyline.common.data.AdvisorConfig;
import org.skyline.common.data.YarnApplication.ApplicationType;
import org.skyline.core.service.AdvisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author sean
 */
@SpringBootApplication
@Configuration
@ComponentScan("org.skyline")
@EnableCaching
@EnableScheduling
@EnableJms
@Slf4j
public class ApplicationEntry {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationEntry.class, args);
  }


  @Bean
  public CaffeineCache advisorConfigCache() {

    return new CaffeineCache(CACHE_NAME,
        Caffeine.newBuilder()
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .build());
  }
}