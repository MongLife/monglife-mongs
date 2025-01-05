package com.monglife.mongs.domain.global.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

@Configuration("taskDataSourceConfig")
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.monglife.mongs.domain.task.repository",
        entityManagerFactoryRef = "taskEntityManager",
        transactionManagerRef = "taskTransactionManager"
)
public class DataSourceConfig {

    @Value("${spring.jpa.task.properties.hibernate.dialect}")
    private String dialect;

    @Value("${spring.jpa.task.properties.hibernate.hbm2ddl.auto}")
    private String ddlAuto;

    @Value("${spring.jpa.task.properties.hibernate.show_sql}")
    private String showSql;

    @Value("${spring.jpa.task.properties.hibernate.format_sql}")
    private String formatSql;

    @Bean(name = "taskDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.task.hikari")
    public HikariConfig taskDataSourceProperties() {
        return new HikariConfig();
    }

    @Bean(name = "taskDataSource")
    public DataSource taskDataSource() {
        return new HikariDataSource(taskDataSourceProperties());
    }

    @Bean(name = "taskJpaProperties")
    public Properties taskJpaProperties(@Qualifier("jpaProperties") Properties properties) {
        Properties jpaProperties = new Properties();
        jpaProperties.put(AvailableSettings.DIALECT, dialect);
        jpaProperties.put(AvailableSettings.HBM2DDL_AUTO, ddlAuto);
        jpaProperties.put(AvailableSettings.SHOW_SQL, showSql);
        jpaProperties.put(AvailableSettings.FORMAT_SQL, formatSql);
        properties.keySet().forEach(field -> jpaProperties.put(field, properties.get(field)));
        return jpaProperties;
    }

    @Bean(name = "taskEntityManager")
    public LocalContainerEntityManagerFactoryBean taskEntityManager(@Qualifier("taskJpaProperties") Properties jpaProperties) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(taskDataSource());
        em.setPackagesToScan("com.monglife.mongs.**.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(jpaProperties);
        return em;
    }

    @Bean(name = "taskTransactionManager")
    public PlatformTransactionManager taskTransactionManager(@Qualifier("taskEntityManager") LocalContainerEntityManagerFactoryBean entityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager.getObject());
        return transactionManager;
    }
}
