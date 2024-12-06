package com.monglife.mongs.domain.mong.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration("mongRedisConfig")
@EnableRedisRepositories(
        basePackages = "com.monglife.mongs.domain.mong.repository",
        redisTemplateRef = "mongRedisTemplate",
        enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP
)
public class RedisConfig {

    @Value("${spring.data.mong.redis.host}")
    private String host;

    @Value("${spring.data.mong.redis.port}")
    private int port;

    @Value("${spring.data.mong.redis.password}")
    private String password;

    @Value("${spring.data.mong.redis.database}")
    private Integer database;

    @Bean("mongRedisConnectionFactory")
    @ConditionalOnMissingBean(name = "mongRedisConnectionFactory")
    public RedisConnectionFactory mongRedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(password);
        redisStandaloneConfiguration.setDatabase(database);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean(name = "mongRedisTemplate")
    public RedisTemplate<String, ?> mongRedisTemplate(@Qualifier("mongRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}
