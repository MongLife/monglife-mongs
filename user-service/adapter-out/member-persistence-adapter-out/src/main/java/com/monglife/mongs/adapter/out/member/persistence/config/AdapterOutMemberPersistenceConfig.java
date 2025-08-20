package com.monglife.mongs.adapter.out.member.persistence.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = "com.monglife.mongs.adapter.out.member.persistence")
@EnableConfigurationProperties(GovTemplateProperty.class)
public class AdapterOutMemberPersistenceConfig {

    @Bean(name = "govRestTemplate")
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, GovTemplateProperty  govTemplateProperty) {
        return restTemplateBuilder
                .rootUri(govTemplateProperty.getUrl())
                .build();
    }
}