package com.mongs.play.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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

@Slf4j
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.mongs.play.domain.collection.repository",
        entityManagerFactoryRef = "collectionEntityManager",
        transactionManagerRef = "collectionTransactionManager"
)
public class CollectionDataSourceConfig {

    @Bean(name = "playerCollectionDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.player-datasource")
    @ConditionalOnMissingBean(name = "playerCollectionDataSourceProperties")
    public DataSourceProperties playerDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "playerCollectionDataSource")
    @ConfigurationProperties(prefix = "spring.player-datasource.hikari")
    @ConditionalOnMissingBean(name = "playerCollectionDataSource")
    public DataSource playerDataSource(@Qualifier("playerCollectionDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "collectionEntityManager")
    public LocalContainerEntityManagerFactoryBean collectionEntityManager(
            @Qualifier("playerCollectionDataSource") DataSource dataSource,
            @Qualifier("entityManagerProperties") Properties properties,
            @Qualifier("hibernateJpaVendorAdapter") HibernateJpaVendorAdapter jpaVendorAdapter
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.mongs.play.domain.collection.entity");
        em.setJpaVendorAdapter(jpaVendorAdapter);
        em.setJpaProperties(properties);
        return em;
    }

    @Bean(name = "collectionTransactionManager")
    public PlatformTransactionManager collectionTransactionManager(@Qualifier("collectionEntityManager") LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(localContainerEntityManagerFactoryBean.getObject());
        return transactionManager;
    }
}
