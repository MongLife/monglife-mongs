package com.monglife.mongs.module.jpa.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Order(999)
@Configuration
@RequiredArgsConstructor
public class MultiTransactionConfig {

    private List<PlatformTransactionManager> transactionManagers;

    @Autowired
    public MultiTransactionConfig(List<PlatformTransactionManager> transactionManagers) {
        this.transactionManagers = transactionManagers;
    }

    @Primary
    @Bean(name = "transactionManager")
    @ConditionalOnMissingBean(ChainedTransactionManager.class)
    public ChainedTransactionManager transactionManager() {
        return new ChainedTransactionManager(transactionManagers.toArray(new PlatformTransactionManager[0]));
    }
}
