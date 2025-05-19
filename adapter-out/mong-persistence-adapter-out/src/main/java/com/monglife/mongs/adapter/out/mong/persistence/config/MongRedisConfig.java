package com.monglife.mongs.adapter.out.mong.persistence.config;

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
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class MongRedisConfig {

    /**
     * const values for config bean name
     */
    private static final String DOMAIN_NAME                   = "mong";
    private static final String REDIS_REPOSITORY_BASE_PACKAGE = "com.monglife.mongs.adapter.out" + DOMAIN_NAME + ".repository";
    private static final String REDIS_REPOSITORY_CONFIG_NAME  = DOMAIN_NAME + "RedisRepositoryConfig";
    private static final String REDIS_CONNECTION_FACTORY_NAME = DOMAIN_NAME + "RedisConnectionFactory";
    private static final String REDIS_TEMPLATE_NAME           = DOMAIN_NAME + "RedisTemplate";

    @Configuration(REDIS_REPOSITORY_CONFIG_NAME)
    @EnableRedisRepositories(
            basePackages = REDIS_REPOSITORY_BASE_PACKAGE,
            redisTemplateRef = REDIS_TEMPLATE_NAME,
            enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP
    )
    public static class RedisRepositoryConfig {}

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
    public RedisConnectionFactory mongRedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(password);
        redisStandaloneConfiguration.setDatabase(database);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean(name = REDIS_TEMPLATE_NAME)
    public RedisTemplate<String, ?> mongRedisTemplate(@Qualifier(REDIS_CONNECTION_FACTORY_NAME) RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}
