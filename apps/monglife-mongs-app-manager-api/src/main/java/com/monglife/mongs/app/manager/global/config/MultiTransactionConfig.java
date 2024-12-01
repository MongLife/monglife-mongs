package com.monglife.mongs.app.manager.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Slf4j
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
    public ChainedTransactionManager transactionManager() {
        return new ChainedTransactionManager(transactionManagers.toArray(new PlatformTransactionManager[0]));
    }
}
