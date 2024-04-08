package com.mongs.management.config;

import com.mongs.core.utils.MongStatusUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongStatusUtilConfig {
    @Bean
    public MongStatusUtil mongStatusUtil() {
        return new MongStatusUtil();
    }
}
