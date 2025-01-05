package com.monglife.mongs.domain.mong.config;

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

@Configuration("mongDataSourceConfig")
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.monglife.mongs.domain.mong.repository",
        entityManagerFactoryRef = "mongEntityManager",
        transactionManagerRef = "mongTransactionManager"
)
public class DataSourceConfig {

    @Value("${spring.jpa.mong.properties.hibernate.dialect}")
    private String dialect;

    @Value("${spring.jpa.mong.properties.hibernate.hbm2ddl.auto}")
    private String ddlAuto;

    @Value("${spring.jpa.mong.properties.hibernate.show_sql}")
    private String showSql;

    @Value("${spring.jpa.mong.properties.hibernate.format_sql}")
    private String formatSql;

    @Bean(name = "mongDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.mong.hikari")
    public HikariConfig mongDataSourceProperties() {
        return new HikariConfig();
    }

    @Bean(name = "mongDataSource")
    public DataSource mongDataSource() {
        return new HikariDataSource(mongDataSourceProperties());
    }

    @Bean(name = "mongJpaProperties")
    public Properties mongJpaProperties(@Qualifier("jpaProperties") Properties properties) {
        Properties jpaProperties = new Properties();
        jpaProperties.put(AvailableSettings.DIALECT, dialect);
        jpaProperties.put(AvailableSettings.HBM2DDL_AUTO, ddlAuto);
        properties.put(AvailableSettings.SHOW_SQL, showSql);
        properties.put(AvailableSettings.FORMAT_SQL, formatSql);
        properties.keySet().forEach(field -> jpaProperties.put(field, properties.get(field)));
        return jpaProperties;
    }

    @Bean(name = "mongEntityManager")
    public LocalContainerEntityManagerFactoryBean mongEntityManager(@Qualifier("mongJpaProperties") Properties jpaProperties) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(mongDataSource());
        em.setPackagesToScan("com.monglife.mongs.**.entity");
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
