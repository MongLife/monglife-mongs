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
            @Qualifier("codeTransactionManager") PlatformTransactionManager codeTransactionManager,
            @Qualifier("mongTransactionManager") PlatformTransactionManager mongTransactionManager
    ) {
        return new ChainedTransactionManager(codeTransactionManager, mongTransactionManager);
    }
}
