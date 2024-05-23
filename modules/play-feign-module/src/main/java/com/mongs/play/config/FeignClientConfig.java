package com.mongs.play.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.mongs.play")
@RequiredArgsConstructor
public class FeignClientConfig {

    private final ObjectMapper objectMapper;

    @Bean
    public FeignErrorDecoder feignErrorDecoder() {
        return new FeignErrorDecoder(objectMapper);
    }
}
