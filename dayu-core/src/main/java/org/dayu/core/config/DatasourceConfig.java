package org.dayu.core.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.persistence.EntityManager;
import org.dayu.core.model.RuntimeConfig;
import org.dayu.core.repository.RuntimeConfigRepository;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

/**
 * @author Sean Liu
 * @date 2019-04-02
 */
@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "coreEntityManagerFactory",
    basePackageClasses = RuntimeConfigRepository.class
)
public class DatasourceConfig {

  @Bean
  @Primary
  @ConfigurationProperties("spring.datasource.core")
  public DataSourceProperties coreDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @Primary
  @ConfigurationProperties("spring.datasource.core.configuration")
  public HikariDataSource coreDataSource() {
    return coreDataSourceProperties().initializeDataSourceBuilder()
        .type(HikariDataSource.class).build();
  }

  @Bean
  @Primary
  public LocalContainerEntityManagerFactoryBean coreEntityManagerFactory(
      EntityManagerFactoryBuilder builder) {
    return builder
        .dataSource(coreDataSource())
        .packages(RuntimeConfig.class)
        .persistenceUnit("cores")
        .build();
  }

  @Primary
  @Bean
  public EntityManager coreEntityManager(EntityManagerFactoryBuilder builder) {
    return coreEntityManagerFactory(builder).getObject().createEntityManager();
  }

}
