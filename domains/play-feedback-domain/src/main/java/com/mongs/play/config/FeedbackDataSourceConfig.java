package com.mongs.play.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.mongs.play.domain.feedback.repository",
        entityManagerFactoryRef = "feedbackEntityManager",
        transactionManagerRef = "feedbackTransactionManager"
)
public class FeedbackDataSourceConfig {

    @Bean(name = "playerFeedbackDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.player-datasource")
    @ConditionalOnMissingBean(name = "playerFeedbackDataSourceProperties")
    public DataSourceProperties playerDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "playerFeedbackDataSource")
    @ConfigurationProperties(prefix = "spring.player-datasource.hikari")
    @ConditionalOnMissingBean(name = "playerFeedbackDataSource")
    public DataSource playerDataSource(@Qualifier("playerFeedbackDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "feedbackEntityManager")
    public LocalContainerEntityManagerFactoryBean feedbackEntityManager(
            @Qualifier("playerFeedbackDataSource") DataSource dataSource,
            @Qualifier("entityManagerProperties") Properties properties,
            @Qualifier("hibernateJpaVendorAdapter") HibernateJpaVendorAdapter jpaVendorAdapter
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.mongs.play.domain.feedback.entity");
        em.setJpaVendorAdapter(jpaVendorAdapter);
        em.setJpaProperties(properties);
        return em;
    }

    @Bean(name = "feedbackTransactionManager")
    public PlatformTransactionManager feedbackTransactionManager(@Qualifier("feedbackEntityManager") LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(localContainerEntityManagerFactoryBean.getObject());
        return transactionManager;
    }
}
