/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.config;

import org.springframework.lang.NonNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Cache factory config
 *
 * @param <T> detail type
 */
public interface CacheFactoryConfig<T> {
    /**
     * Cache
     *
     * @return {@link Boolean} true if cache
     */
    default boolean cache() {
        return true;
    }

    /**
     * Detail class
     *
     * @return {@link Class}
     */
    @NonNull
    Class<? extends T> objectClass();

    /**
     * Cache name
     *
     * @return {@link String}
     */
    default String cacheName() {
        return objectClass().getSimpleName();
    }

    /**
     * Cache name collection
     *
     * @return {@link String}
     */
    default String cacheNameList() {
        return "List".concat(objectClass().getSimpleName());
    }

    /**
     * Cache name paging
     *
     * @return {@link String}
     */
    default String cacheNamePaging() {
        return "Page".concat(objectClass().getSimpleName());
    }

    /**
     * Cache name Search
     *
     * @return {@link String}
     */
    default String cacheNameSearch() {
        return "Search".concat(objectClass().getSimpleName());
    }

    /**
     * Time to live single cache
     *
     * @return {@link Duration}
     */
    default Duration singleTtl() {
        return Duration.ofSeconds(50);
    }

    /**
     * Time to live collection cache
     *
     * @return {@link Duration}
     */
    default Duration collectionTtl() {
        return Duration.ofSeconds(5);
    }

    /**
     * Cache getList
     *
     * @return {@link Boolean} true if cache getList
     */
    default boolean getList() {
        return true;
    }

    /**
     * Cache getListFilter
     *
     * @return {@link Boolean} true if cache getListFilter
     */
    default boolean getListFilter() {
        return true;
    }

    /**
     * Cache getDetail
     *
     * @return {@link Boolean} true if cache getDetail
     */
    default boolean getModel() {
        return true;
    }

    /**
     * Cache getPaging
     *
     * @return {@link Boolean} true if cache getPaging
     */
    default boolean getPaging() {
        return true;
    }

    /**
     * Cache getSearch
     *
     * @return {@link Boolean} true if cache getPaging
     */
    default boolean getSearch() {
        return true;
    }

    /**
     * Cache existByFilter
     *
     * @return {@link Boolean} true if cache existByFilter
     */
    default boolean existByFilter() {
        return true;
    }

    /**
     * Cache existByDetail
     *
     * @return {@link Boolean} true if cache existByDetail
     */
    default boolean existByDetail() {
        return true;
    }

    /**
     * List object cache forward
     *
     * @return list class object forward
     */
    default List<Class<?>> cacheForward() {
        return new ArrayList<>();
    }

    /**
     * @return collection name forward
     */
    default List<String> cacheListForward() {
        return cacheForward().stream()
                .map(
                        clazz -> "List".concat(clazz.getSimpleName())
                ).toList();
    }

    /**
     * @return Search name forward
     */
    default List<String> cacheSearchForward() {
        return cacheForward().stream()
                .map(
                        clazz -> "Search".concat(clazz.getSimpleName())
                ).toList();
    }

    /**
     * @return Search name forward
     */
    default List<String> cachePageForward() {
        return cacheForward().stream()
                .map(
                        clazz -> "Page".concat(clazz.getSimpleName())
                ).toList();
    }

    /**
     * @return cacheName forward
     */
    default List<String> cacheNameForward() {
        return cacheForward().stream()
                .map(
                        Class::getSimpleName
                ).toList();
    }

}
