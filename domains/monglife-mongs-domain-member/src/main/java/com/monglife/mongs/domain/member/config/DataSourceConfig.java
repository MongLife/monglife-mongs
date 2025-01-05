package com.monglife.mongs.domain.member.config;

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

@Configuration("memberDataSourceConfig")
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.monglife.mongs.domain.member.repository",
        entityManagerFactoryRef = "memberEntityManager",
        transactionManagerRef = "memberTransactionManager"
)
public class DataSourceConfig {

    @Value("${spring.jpa.member.properties.hibernate.dialect}")
    private String dialect;

    @Value("${spring.jpa.member.properties.hibernate.hbm2ddl.auto}")
    private String ddlAuto;

    @Value("${spring.jpa.member.properties.hibernate.show_sql}")
    private String showSql;

    @Value("${spring.jpa.member.properties.hibernate.format_sql}")
    private String formatSql;

    @Bean(name = "memberDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.member.hikari")
    public HikariConfig memberDataSourceProperties() {
        return new HikariConfig();
    }

    @Bean(name = "memberDataSource")
    public DataSource memberDataSource() {
        return new HikariDataSource(memberDataSourceProperties());
    }

    @Bean(name = "memberJpaProperties")
    public Properties memberJpaProperties(@Qualifier("jpaProperties") Properties properties) {
        Properties jpaProperties = new Properties();
        jpaProperties.put(AvailableSettings.DIALECT, dialect);
        jpaProperties.put(AvailableSettings.HBM2DDL_AUTO, ddlAuto);
        properties.put(AvailableSettings.SHOW_SQL, showSql);
        properties.put(AvailableSettings.FORMAT_SQL, formatSql);
        properties.keySet().forEach(field -> jpaProperties.put(field, properties.get(field)));
        return jpaProperties;
    }

    @Bean(name = "memberEntityManager")
    public LocalContainerEntityManagerFactoryBean memberEntityManager(@Qualifier("memberJpaProperties") Properties jpaProperties) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(memberDataSource());
        em.setPackagesToScan("com.monglife.mongs.**.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(jpaProperties);
        return em;
    }

    @Bean(name = "memberTransactionManager")
    public PlatformTransactionManager memberTransactionManager(@Qualifier("memberEntityManager") LocalContainerEntityManagerFactoryBean entityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager.getObject());
        return transactionManager;
    }
}
