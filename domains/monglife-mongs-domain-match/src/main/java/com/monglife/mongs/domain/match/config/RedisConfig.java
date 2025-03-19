package com.monglife.mongs.domain.match.config;

import com.monglife.mongs.domain.match.entity.MatchingEntity;
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
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration("matchRedisConfig")
@EnableRedisRepositories(
        basePackages = "com.monglife.mongs.domain.match.repository",
        redisTemplateRef = "matchRedisTemplate",
        enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP
)
public class RedisConfig {

    @Value("${spring.data.match.redis.host}")
    private String host;

    @Value("${spring.data.match.redis.port}")
    private int port;

    @Value("${spring.data.match.redis.password}")
    private String password;

    @Value("${spring.data.match.redis.database}")
    private Integer database;

    @Bean("matchRedisConnectionFactory")
    @ConditionalOnMissingBean(name = "matchRedisConnectionFactory")
    public RedisConnectionFactory matchRedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(password);
        redisStandaloneConfiguration.setDatabase(database);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean(name = "matchRedisTemplate")
    public RedisTemplate<String, ?> matchRedisTemplate(@Qualifier("matchRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(MatchingEntity.class));
        return redisTemplate;
    }
}
