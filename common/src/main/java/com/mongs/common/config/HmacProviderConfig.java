package com.mongs.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.core.utils.HmacProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class HmacProviderConfig {

    private final ObjectMapper objectMapper;
    private final String secretKey = "E32723575+MIGQkE5D+hlIlStxSSmA+7z32MPidF";
    @Bean
    public HmacProvider hmacProvider() {
        return new HmacProvider(objectMapper, secretKey);
    }
}
