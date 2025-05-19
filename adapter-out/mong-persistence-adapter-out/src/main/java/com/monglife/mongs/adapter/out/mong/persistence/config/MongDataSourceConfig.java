package com.monglife.mongs.adapter.out.mong.persistence.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
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

@Configuration
@EnableTransactionManagement
public class MongDataSourceConfig {

    /**
     * const values for config bean name
     */
    private static final String DOMAIN_NAME                 = "mong";
    private static final String COMMON_ENTITY_BASE_PACKAGES = "com.monglife.module.common.jpa.entity";
    private static final String ENTITY_BASE_PACKAGES        = "com.monglife.mongs.adapter.out." + DOMAIN_NAME + ".persistence.entity";
    private static final String REPOSITORY_BASE_PACKAGES    = "com.monglife.mongs.adapter.out." + DOMAIN_NAME + ".persistence.repository";
    private static final String JPA_PROPERTIES_NAME         = DOMAIN_NAME + "JpaProperties";
    private static final String DATASOURCE_PROPERTIES_NAME  = DOMAIN_NAME + "DataSourceProperties";
    private static final String DATASOURCE_NAME             = DOMAIN_NAME + "DataSource";
    private static final String ENTITY_MANAGER_NAME         = DOMAIN_NAME + "EntityManager";
    private static final String TRANSACTION_MANAGER_NAME    = DOMAIN_NAME + "TransactionManager";
    private static final String JPA_REPOSITORY_CONFIG_NAME  = DOMAIN_NAME + "JpaRepositoryConfig";
    private static final String JPA_QUERY_FACTORY_NAME      = DOMAIN_NAME + "JpaQueryFactory";
    private static final String HIBERNATE_PROPERTIES_NAME   = "hibernateProperties";
    private static final String HIKARI_PROPERTIES_PREFIX    = "spring.datasource." + DOMAIN_NAME + ".hikari";

    @Configuration(JPA_REPOSITORY_CONFIG_NAME)
    @EnableJpaRepositories(
            entityManagerFactoryRef = ENTITY_MANAGER_NAME,
            transactionManagerRef   = TRANSACTION_MANAGER_NAME,
            basePackages            = REPOSITORY_BASE_PACKAGES
    )
    public static class JpaRepositoryConfig {}

    @Value("${spring.jpa." + DOMAIN_NAME + ".properties.hibernate.dialect}")
    private String dialect;

    @Value("${spring.jpa." + DOMAIN_NAME + ".properties.hibernate.hbm2ddl.auto}")
    private String ddlAuto;

    @Value("${spring.jpa." + DOMAIN_NAME + ".properties.hibernate.show_sql}")
    private String showSql;

    @Value("${spring.jpa." + DOMAIN_NAME + ".properties.hibernate.format_sql}")
    private String formatSql;

    /**
     * Hikari 속성 Bean 생성
     * @return Hikari 속성 객체
     */
    @Bean(name = DATASOURCE_PROPERTIES_NAME)
    @ConfigurationProperties(prefix = HIKARI_PROPERTIES_PREFIX)
    public HikariConfig dataSourceProperties() {
        return new HikariConfig();
    }

    /**
     * Database DataSource Bean 생성
     * @param dataSourceProperties dataSource 속성 객체
     * @return Database DataSource 객체
     */
    @Bean(name = DATASOURCE_NAME)
    public DataSource dataSource(@Qualifier(DATASOURCE_PROPERTIES_NAME) HikariConfig dataSourceProperties) {
        return new HikariDataSource(dataSourceProperties);
    }

    /**
     * Jpa 속성 Bean 생성
     * @param hibernateProperties hibernate 속성 객체
     * @param beanFactory Spring Bean Factory
     * @return Jpa 속성 객체
     */
    @Bean(name = JPA_PROPERTIES_NAME)
    public Properties jpaProperties(@Qualifier(HIBERNATE_PROPERTIES_NAME) Properties hibernateProperties, ConfigurableListableBeanFactory beanFactory) {
        Properties jpaProperties = new Properties();
        jpaProperties.put(AvailableSettings.DIALECT, dialect);
        jpaProperties.put(AvailableSettings.HBM2DDL_AUTO, ddlAuto);
        jpaProperties.put(AvailableSettings.SHOW_SQL, showSql);
        jpaProperties.put(AvailableSettings.FORMAT_SQL, formatSql);
        jpaProperties.put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(beanFactory));
        jpaProperties.putAll(hibernateProperties);
        return jpaProperties;
    }

    /**
     * Jpa Entity Manager Bean 생성
     * @param dataSource Database DataSource Bean
     * @param jpaProperties Jpa 속성 Bean
     * @return Jpa Entity Manager Factory 객체
     */
    @Bean(name = ENTITY_MANAGER_NAME)
    public LocalContainerEntityManagerFactoryBean entityManager(@Qualifier(DATASOURCE_NAME) DataSource dataSource, @Qualifier(JPA_PROPERTIES_NAME) Properties jpaProperties) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(ENTITY_BASE_PACKAGES, COMMON_ENTITY_BASE_PACKAGES);
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(jpaProperties);
        return em;
    }

    /**
     * Transaction Manager Bean 생성
     * @param entityManager Jpa Entity Manager
     * @return Transaction Manager 객체
     */
    @Bean(name = TRANSACTION_MANAGER_NAME)
    public PlatformTransactionManager transactionManager(@Qualifier(ENTITY_MANAGER_NAME) LocalContainerEntityManagerFactoryBean entityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager.getObject());
        return transactionManager;
    }

    @Bean(name = JPA_QUERY_FACTORY_NAME)
    public JPAQueryFactory jpaQueryFactory(@Qualifier(ENTITY_MANAGER_NAME) EntityManager entityManager){
        return new JPAQueryFactory(entityManager);
    }
}
