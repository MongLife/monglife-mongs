package com.monglife.mongs.module.jpa.config;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.Properties;

@Configuration
public class HibernateConfig {

    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String dialect;

    @Value("${spring.jpa.properties.hibernate.hbm2ddl.auto}")
    private String ddlAuto;

    @Value("${spring.jpa.properties.hibernate.show_sql}")
    private String showSql;


    @Bean(name = "hibernateJpaVendorAdapter")
    public HibernateJpaVendorAdapter hibernateJpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean(name = "jpaProperties")
    public Properties jpaProperties(ConfigurableListableBeanFactory beanFactory) {
        Properties properties = new Properties();
        properties.put(AvailableSettings.DIALECT, dialect);                 // "hibernate.dialect"
        properties.put(AvailableSettings.HBM2DDL_AUTO, ddlAuto);            // "hibernate.hbm2ddl.auto"
        properties.put(AvailableSettings.SHOW_SQL, showSql);                // "hibernate.show_sql"
        properties.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY, "com.monglife.mongs.module.jpa.config.ImprovedNamingStrategy");   // "hibernate.physical_naming_strategy"
        properties.put(AvailableSettings.FORMAT_SQL, true);
        properties.put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(beanFactory));
        return properties;
    }
}
