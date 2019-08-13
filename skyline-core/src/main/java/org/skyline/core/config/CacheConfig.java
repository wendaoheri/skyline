package org.skyline.core.config;

import static org.skyline.core.service.AdvisorService.CACHE_NAME;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sean Liu
 * @date 2019-08-13
 */
@Configuration
public class CacheConfig {

  @Bean
  public CaffeineCache advisorConfigCache() {

    return new CaffeineCache(CACHE_NAME,
        Caffeine.newBuilder()
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .build());
  }
}
