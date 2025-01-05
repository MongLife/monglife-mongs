package com.monglife.mongs.domain.device.config;

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

@Configuration("deviceDataSourceConfig")
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.monglife.mongs.domain.device.repository",
        entityManagerFactoryRef = "deviceEntityManager",
        transactionManagerRef = "deviceTransactionManager"
)
public class DataSourceConfig {

    @Value("${spring.jpa.device.properties.hibernate.dialect}")
    private String dialect;

    @Value("${spring.jpa.device.properties.hibernate.hbm2ddl.auto}")
    private String ddlAuto;

    @Value("${spring.jpa.device.properties.hibernate.show_sql}")
    private String showSql;

    @Value("${spring.jpa.device.properties.hibernate.format_sql}")
    private String formatSql;

    @Bean(name = "deviceDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.device.hikari")
    public HikariConfig deviceDataSourceProperties() {
        return new HikariConfig();
    }

    @Bean(name = "deviceDataSource")
    public DataSource deviceDataSource() {
        return new HikariDataSource(deviceDataSourceProperties());
    }

    @Bean(name = "deviceJpaProperties")
    public Properties deviceJpaProperties(@Qualifier("jpaProperties") Properties properties) {
        Properties jpaProperties = new Properties();
        jpaProperties.put(AvailableSettings.DIALECT, dialect);
        jpaProperties.put(AvailableSettings.HBM2DDL_AUTO, ddlAuto);
        properties.put(AvailableSettings.SHOW_SQL, showSql);
        properties.put(AvailableSettings.FORMAT_SQL, formatSql);
        properties.keySet().forEach(field -> jpaProperties.put(field, properties.get(field)));
        return jpaProperties;
    }

    @Bean(name = "deviceEntityManager")
    public LocalContainerEntityManagerFactoryBean deviceEntityManager(@Qualifier("deviceJpaProperties") Properties jpaProperties) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(deviceDataSource());
        em.setPackagesToScan("com.monglife.mongs.**.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(jpaProperties);
        return em;
    }

    @Bean(name = "deviceTransactionManager")
    public PlatformTransactionManager deviceTransactionManager(@Qualifier("deviceEntityManager") LocalContainerEntityManagerFactoryBean entityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager.getObject());
        return transactionManager;
    }
}
