package com.monglife.mongs.module.feign.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Retryer;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
@EnableFeignClients(basePackages = { "com.monglife.mongs" })
public class FeignClientConfig {

    private static final Long CONNECT_TIMEOUT = 2000L;

    private static final Long READ_TIMEOUT = 4000L;

    /**
     * Feign Client 기본 (?) client 는 Patch Method 사용 불가 -> ApacheHttp5Client 사용으로 변경
     * ApacheHttp5Client 는 @LoadBalanced 기능이 없음
     * restTemplate Bean 으로 등록해서 로드 밸런싱 가능한 restTemplate 정의
     */
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS, READ_TIMEOUT, TimeUnit.MILLISECONDS, false);
    }

    @Bean
    public Retryer retryer() {
        return Retryer.NEVER_RETRY;
    }

    @Bean
    public FeignErrorDecoder feignErrorDecoder() {
        return new FeignErrorDecoder(new ObjectMapper());
    }

    @Bean
    public FeignInterceptor feignInterceptor() {
        return new FeignInterceptor();
    }
}
