/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.factory.data;


import com.thanhvh.exception.InvalidException;

/**
 * IPersistDataFactory
 *
 * @param <I> info type
 * @param <D> detail type
 * @param <E> entity type
 */
public interface IPersistDataFactory<I, D extends I, E> {
    /**
     * Convert detail to entity when create
     *
     * @param detail detail model
     * @return entity
     * @throws InvalidException invalid
     */
    E createConvertToEntity(D detail) throws InvalidException;

    /**
     * Convert detail to entity when update
     *
     * @param entity old entity
     * @param detail detail
     * @throws InvalidException invalid
     */
    void updateConvertToEntity(E entity, D detail) throws InvalidException;

    /**
     * Convert entity to detail model
     *
     * @param entity entity
     * @return detail model
     * @throws InvalidException invalid
     */
    D convertToDetail(E entity) throws InvalidException;

    /**
     * Convert entity to info model
     *
     * @param entity entity
     * @return info model
     * @throws InvalidException invalid
     */
    I convertToInfo(E entity) throws InvalidException;
}
