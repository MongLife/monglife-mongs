package com.monglife.mongs.adapter.out.device.cache.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class DeviceRedisConfig {

    /**
     * const values for config bean name
     */
    private static final String DOMAIN_NAME                   = "device";
    private static final String REDIS_CONNECTION_FACTORY_NAME = DOMAIN_NAME + "RedisConnectionFactory";
    private static final String REDIS_TEMPLATE_NAME           = DOMAIN_NAME + "RedisTemplate";

    @Value("${spring.data." + DOMAIN_NAME + ".redis.host}")
    private String host;

    @Value("${spring.data." + DOMAIN_NAME + ".redis.port}")
    private int port;

    @Value("${spring.data." + DOMAIN_NAME + ".redis.password}")
    private String password;

    @Value("${spring.data." + DOMAIN_NAME + ".redis.database}")
    private Integer database;

    @Bean(name = REDIS_CONNECTION_FACTORY_NAME)
    @ConditionalOnMissingBean(name = REDIS_CONNECTION_FACTORY_NAME)
    public RedisConnectionFactory deviceRedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(password);
        redisStandaloneConfiguration.setDatabase(database);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    /**
     * 일일 환전량 카운터 전용 템플릿
     *
     * 값이 정수 카운터 하나뿐이라 JSON 직렬화가 필요 없다. StringRedisTemplate 를 쓰면
     * INCRBY / DECRBY 가 Redis 쪽 원자 연산으로 그대로 내려간다.
     */
    @Bean(name = REDIS_TEMPLATE_NAME)
    public StringRedisTemplate deviceRedisTemplate(@Qualifier(REDIS_CONNECTION_FACTORY_NAME) RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }
}
