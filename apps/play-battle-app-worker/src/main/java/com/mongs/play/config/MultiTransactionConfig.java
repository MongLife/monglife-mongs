package com.mongs.play.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MultiTransactionConfig {

    @Primary
    @Bean(name = "transactionManager")
    public ChainedTransactionManager transactionManager(
            @Qualifier("mongTransactionManager") PlatformTransactionManager mongTransactionManager,
            @Qualifier("battleTransactionManager") PlatformTransactionManager battleTransactionManager
    ) {
        return new ChainedTransactionManager(mongTransactionManager, battleTransactionManager);
    }
}
