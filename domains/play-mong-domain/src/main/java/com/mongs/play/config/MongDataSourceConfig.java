package com.mongs.play.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories(
        basePackages = "com.mongs.play.domain.mong.repository",
        entityManagerFactoryRef = "mongEntityManager",
        transactionManagerRef = "mongTransactionManager"
)
public class MongDataSourceConfig {

    @Bean(name = "managementMongDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.management-datasource")
    @ConditionalOnMissingBean(name = "managementMongDataSourceProperties")
    public DataSourceProperties managementMongDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "managementMongDataSource")
    @ConfigurationProperties(prefix = "spring.management-datasource.hikari")
    @ConditionalOnMissingBean(name = "managementMongDataSource")
    public DataSource managementMongDataSource(@Qualifier("managementMongDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "mongEntityManager")
    public LocalContainerEntityManagerFactoryBean mongEntityManager(
            @Qualifier("managementMongDataSource") DataSource dataSource,
            @Qualifier("entityManagerProperties") Properties properties,
            @Qualifier("hibernateJpaVendorAdapter") HibernateJpaVendorAdapter jpaVendorAdapter
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.mongs.play.domain.mong.entity");
        em.setJpaVendorAdapter(jpaVendorAdapter);
        em.setJpaProperties(properties);
        return em;
    }

    @Bean(name = "mongTransactionManager")
    public PlatformTransactionManager mongTransactionManager(@Qualifier("mongEntityManager") LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(localContainerEntityManagerFactoryBean.getObject());
        return transactionManager;
    }
}
