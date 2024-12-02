package com.monglife.mongs.module.jpa.config;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.util.Instantiator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.Properties;

@Configuration
public class HibernateConfig {

    @Bean(name = "jpaProperties")
    public Properties jpaProperties(ConfigurableListableBeanFactory beanFactory) {
        Properties properties = new Properties();
        properties.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY, "com.monglife.mongs.module.jpa.config.ImprovedNamingStrategy");
        properties.put(AvailableSettings.FORMAT_SQL, true);
        properties.put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(beanFactory));
        return properties;
    }

    @Bean(name = "hibernateJpaVendorAdapter")
    public HibernateJpaVendorAdapter hibernateJpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }
}
