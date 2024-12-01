package com.monglife.mongs.domain.mong.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
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

@Configuration("mongDataSourceConfig")
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories(
        basePackages =  "com.monglife.mongs.domain.mong.repository",
        entityManagerFactoryRef = "mongEntityManager",
        transactionManagerRef = "mongTransactionManager"
)
public class DataSourceConfig {

    @Bean(name = "mongDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties mongDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "mongDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource mongDataSource() {
        return mongDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "mongEntityManager")
    public LocalContainerEntityManagerFactoryBean mongEntityManager(@Qualifier("jpaProperties") Properties jpaProperties) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(mongDataSource());
        em.setPackagesToScan("com.monglife.mongs.domain.mong.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(jpaProperties);
        return em;
    }

    @Bean(name = "mongTransactionManager")
    public PlatformTransactionManager mongTransactionManager(@Qualifier("mongEntityManager") LocalContainerEntityManagerFactoryBean entityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager.getObject());
        return transactionManager;
    }
}
