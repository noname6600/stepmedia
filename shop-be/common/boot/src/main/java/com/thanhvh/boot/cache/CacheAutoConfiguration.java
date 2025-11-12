package com.thanhvh.boot.cache;

import com.thanhvh.cache.ITimeRedisCacheManager;
import com.thanhvh.cache.ITimeRedisCacheManagerFactory;
import com.thanhvh.cache.TimeRedisCacheManagerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * Cache config
 */
@AutoConfiguration
@EnableConfigurationProperties(CacheProperties.class)
@ConditionalOnBean(RedisConnectionFactory.class)
@ConditionalOnProperty(name = "spring.data.redis", matchIfMissing = true)
public class CacheAutoConfiguration {
    private CacheAutoConfiguration() {
    }

    /**
     * Create factory
     *
     * @param cacheProperties         cacheProperties
     * @param redisCacheConfiguration redisCache configuration
     * @param redisConnectionFactory  connection factory
     * @param resourceLoader          resource loader
     * @param environment             environment
     * @return Cache Manager factory
     */
    @Bean
    @ConditionalOnMissingBean
    public ITimeRedisCacheManagerFactory cacheManagerFactory(
            CacheProperties cacheProperties,
            ObjectProvider<RedisCacheConfiguration> redisCacheConfiguration,
            RedisConnectionFactory redisConnectionFactory,
            ResourceLoader resourceLoader,
            Environment environment
    ) {
        return new TimeRedisCacheManagerFactory(
                cacheProperties,
                redisCacheConfiguration,
                redisConnectionFactory,
                resourceLoader,
                environment
        );
    }

    /**
     * Create cache manager
     *
     * @param factory Redis cache manager factory
     * @return manager
     */
    @Bean
    @ConditionalOnMissingBean
    public ITimeRedisCacheManager cacheManager(ITimeRedisCacheManagerFactory factory) {
        return factory.cacheManager();
    }
}
