package org.dayu.plugin.schedule.dsp;

import com.zaxxer.hikari.HikariDataSource;
import javax.persistence.EntityManager;
import org.dayu.plugin.schedule.dsp.model.JobApplicationLog;
import org.dayu.plugin.schedule.dsp.repository.JobApplicationLogRepository;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

/**
 * @author Sean Liu
 * @date 2019-04-02
 */
@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "scheduleEntityManagerFactory",
    basePackageClasses = JobApplicationLogRepository.class
)
public class DataSourceConfig {

  @Bean
  @ConfigurationProperties("spring.datasource.schedule")
  public DataSourceProperties scheduleDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @ConfigurationProperties("spring.datasource.schedule.configuration")
  public HikariDataSource scheduleDataSource() {
    return scheduleDataSourceProperties().initializeDataSourceBuilder()
        .type(HikariDataSource.class).build();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean scheduleEntityManagerFactory(
      EntityManagerFactoryBuilder builder) {
    return builder
        .dataSource(scheduleDataSource())
        .packages(JobApplicationLog.class)
        .persistenceUnit("schedules")
        .build();
  }

  @Bean
  public EntityManager scheduleEntityManager(EntityManagerFactoryBuilder builder) {
    return scheduleEntityManagerFactory(builder).getObject().createEntityManager();
  }
}
