package com.monglife.mongs.domain.match.config;

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

@Configuration("matchDataSourceConfig")
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.monglife.mongs.domain.match.repository",
        entityManagerFactoryRef = "matchEntityManager",
        transactionManagerRef = "matchTransactionManager"
)
public class DataSourceConfig {

    @Value("${spring.jpa.match.properties.hibernate.dialect}")
    private String dialect;

    @Value("${spring.jpa.match.properties.hibernate.hbm2ddl.auto}")
    private String ddlAuto;

    @Value("${spring.jpa.match.properties.hibernate.show_sql}")
    private String showSql;


    @Bean(name = "matchDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.match.hikari")
    public HikariConfig matchDataSourceProperties() {
        return new HikariConfig();
    }

    @Bean(name = "matchDataSource")
    public DataSource matchDataSource() {
        return new HikariDataSource(matchDataSourceProperties());
    }

    @Bean(name = "matchJpaProperties")
    public Properties matchJpaProperties(@Qualifier("jpaProperties") Properties properties) {
        Properties jpaProperties = new Properties();
        jpaProperties.put(AvailableSettings.DIALECT, dialect);
        jpaProperties.put(AvailableSettings.HBM2DDL_AUTO, ddlAuto);
        jpaProperties.put(AvailableSettings.SHOW_SQL, showSql);
        properties.keySet().forEach(field -> jpaProperties.put(field, properties.get(field)));
        return jpaProperties;
    }

    @Bean(name = "matchEntityManager")
    public LocalContainerEntityManagerFactoryBean matchEntityManager(@Qualifier("matchJpaProperties") Properties jpaProperties) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(matchDataSource());
        em.setPackagesToScan("com.monglife.mongs.**.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(jpaProperties);
        return em;
    }

    @Bean(name = "matchTransactionManager")
    public PlatformTransactionManager matchTransactionManager(@Qualifier("matchEntityManager") LocalContainerEntityManagerFactoryBean entityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager.getObject());
        return transactionManager;
    }
}
