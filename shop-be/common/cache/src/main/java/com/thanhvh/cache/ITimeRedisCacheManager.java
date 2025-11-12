/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.RedisConnectionFailureException;

import java.time.Duration;

/**
 * Time redis cache manager
 */
public interface ITimeRedisCacheManager extends CacheManager {
    /**
     * @param cacheName     cache name
     * @param key           key
     * @param responseClass response class
     * @param <F>           responseClass
     * @return value
     * @throws CreateCacheException when create or get cache fail
     */
    default <F> F get(String cacheName, Object key, Class<F> responseClass) throws CreateCacheException {
        Cache cache = getCache(cacheName);
        if (cache == null) {
            throw new CreateCacheException("Cannot get cache " + cacheName);
        }
        if (isCacheUnAvailable()) {
            clearCacheFail();
        }
        try {
            return cache.get(key, responseClass);
        } catch (RedisConnectionFailureException exception) {
            cacheUnAvailable(cacheName);
            return null;
        }
    }

    /**
     * @param cacheName cache name
     * @param key       key
     * @param value     value
     * @param ttl       time to live
     * @throws CreateCacheException when create or get cache fail
     */
    default void put(String cacheName, Object key, Object value, Duration ttl) throws CreateCacheException {
        TimeRedisCache cache = (TimeRedisCache) getCache(cacheName);
        if (cache == null) {
            throw new CreateCacheException("Cannot get cache " + cacheName);
        }
        if (isCacheUnAvailable()) {
            clearCacheFail();
        }
        try {
            cache.put(key, value, ttl);
        } catch (RedisConnectionFailureException exception) {
            cacheUnAvailable(cacheName);
        }
    }

    /**
     * cache evict
     *
     * @param cacheName cache name
     * @param key       key
     * @throws CreateCacheException when create or get cache fail
     */
    default void evict(String cacheName, Object key) throws CreateCacheException {
        Cache cache = getCache(cacheName);
        if (cache == null) {
            throw new CreateCacheException("Cannot get cache " + cacheName);
        }
        if (isCacheUnAvailable()) {
            clearCacheFail();
        }
        try {
            cache.evict(key);
        } catch (RedisConnectionFailureException exception) {
            cacheUnAvailable(cacheName);
        }
    }

    /**
     * clear cache name
     *
     * @param cacheName cache name
     * @throws CreateCacheException when create or get cache fail
     */
    default void clear(String cacheName) throws CreateCacheException {
        Cache cache = getCache(cacheName);
        if (cache == null) {
            throw new CreateCacheException("Cannot get cache " + cacheName);
        }
        if (isCacheUnAvailable()) {
            clearCacheFail();
        }
        try {
            cache.clear();
        } catch (RedisConnectionFailureException exception) {
            cacheUnAvailable(cacheName);
        }
    }


    /**
     * Cache unavailable
     *
     * @param cacheName cache name
     */
    void cacheUnAvailable(String cacheName);

    /**
     * Get redis cache status
     *
     * @return boolean
     */
    boolean isCacheUnAvailable();

    /**
     * Clear cache fail
     */
    void clearCacheFail();
}
