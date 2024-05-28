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
    @Bean(name = "chainedTransactionManager")
    public ChainedTransactionManager chainedTransactionManager(
            @Qualifier("codeTransactionManager") PlatformTransactionManager codeTransactionManager,
            @Qualifier("collectionTransactionManager") PlatformTransactionManager collectionTransactionManager,
            @Qualifier("feedbackTransactionManager") PlatformTransactionManager feedbackTransactionManager,
            @Qualifier("memberTransactionManager") PlatformTransactionManager memberTransactionManager,
            @Qualifier("paymentTransactionManager") PlatformTransactionManager paymentTransactionManager
    ) {
        return new ChainedTransactionManager(codeTransactionManager, collectionTransactionManager, feedbackTransactionManager, memberTransactionManager, paymentTransactionManager);
    }
}
