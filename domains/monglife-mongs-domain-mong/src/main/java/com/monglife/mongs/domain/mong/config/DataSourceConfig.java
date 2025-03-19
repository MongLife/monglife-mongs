package com.monglife.mongs.domain.mong.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration("mongDataSourceConfig")
@EnableTransactionManagement
public class DataSourceConfig {

    private static final String DOMAIN_NAME                = "mong";
    private static final String REPOSITORY_BASE_PACKAGES   = "com.monglife.mongs.domain." + DOMAIN_NAME + ".repository";
    private static final String JPA_PROPERTIES_NAME        = DOMAIN_NAME + "JpaProperties";
    private static final String DATASOURCE_PROPERTIES_NAME = DOMAIN_NAME + "DataSourceProperties";
    private static final String DATASOURCE_NAME            = DOMAIN_NAME + "DataSource";
    private static final String ENTITY_MANAGER_NAME        = DOMAIN_NAME + "EntityManager";
    private static final String TRANSACTION_MANAGER_NAME   = DOMAIN_NAME + "TransactionManager";

    @Configuration("mongJpaRepositoryConfig")
    @EnableJpaRepositories(
            basePackages = REPOSITORY_BASE_PACKAGES,
            entityManagerFactoryRef = ENTITY_MANAGER_NAME,
            transactionManagerRef = TRANSACTION_MANAGER_NAME
    )
    public static class JpaMongRepositoryConfig {}

    @Value("${spring.jpa." + DOMAIN_NAME + ".properties.hibernate.dialect}")
    private String dialect;

    @Value("${spring.jpa." + DOMAIN_NAME + ".properties.hibernate.hbm2ddl.auto}")
    private String ddlAuto;

    @Value("${spring.jpa." + DOMAIN_NAME + ".properties.hibernate.show_sql}")
    private String showSql;

    @Value("${spring.jpa." + DOMAIN_NAME + ".properties.hibernate.format_sql}")
    private String formatSql;

    @Bean(name =  DATASOURCE_PROPERTIES_NAME)
    @ConfigurationProperties(prefix = "spring.datasource." + DOMAIN_NAME + ".hikari")
    public HikariConfig deviceDataSourceProperties() {
        return new HikariConfig();
    }

    @Bean(name = DATASOURCE_NAME)
    public DataSource dataSource(@Qualifier(DATASOURCE_PROPERTIES_NAME) HikariConfig dataSourceProperties) {
        return new HikariDataSource(dataSourceProperties);
    }

    @Bean(name = JPA_PROPERTIES_NAME)
    public Properties jpaProperties(@Qualifier("hibernateProperties") Properties hibernateProperties, ConfigurableListableBeanFactory beanFactory) {
        Properties jpaProperties = new Properties();
        jpaProperties.put(AvailableSettings.DIALECT, dialect);
        jpaProperties.put(AvailableSettings.HBM2DDL_AUTO, ddlAuto);
        jpaProperties.put(AvailableSettings.SHOW_SQL, showSql);
        jpaProperties.put(AvailableSettings.FORMAT_SQL, formatSql);
        jpaProperties.put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(beanFactory));
        jpaProperties.putAll(hibernateProperties);
        return jpaProperties;
    }

    @Bean(name = ENTITY_MANAGER_NAME)
    public LocalContainerEntityManagerFactoryBean entityManager(@Qualifier(DATASOURCE_NAME) DataSource dataSource, @Qualifier(JPA_PROPERTIES_NAME) Properties jpaProperties) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.monglife.**.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(jpaProperties);
        return em;
    }

    @Bean(name = TRANSACTION_MANAGER_NAME)
    public PlatformTransactionManager transactionManager(@Qualifier(ENTITY_MANAGER_NAME) LocalContainerEntityManagerFactoryBean entityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager.getObject());
        return transactionManager;
    }
}
