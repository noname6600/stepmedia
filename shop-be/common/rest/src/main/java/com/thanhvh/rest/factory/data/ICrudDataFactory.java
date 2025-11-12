/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.factory.data;

import com.thanhvh.exception.InvalidException;
import com.thanhvh.rest.filter.IFilter;
import com.thanhvh.rest.model.IBaseData;

/**
 * Crud factory
 *
 * @param <I> id type
 * @param <T> info type
 * @param <U> detail type
 */
public interface ICrudDataFactory<I, T extends IBaseData<I>, U extends T> extends IFactory<I, T, U> {
    /**
     * Create model
     *
     * @param detail detail object
     * @return Model after create
     * @throws InvalidException invalid
     */
    U createModel(U detail) throws InvalidException;

    /**
     * Update model
     *
     * @param detail detail object
     * @return Model after create
     * @throws InvalidException invalid
     */
    U updateModel(U detail) throws InvalidException;

    /**
     * Delete model
     *
     * @param id   id object
     * @param data filter
     * @param <F>  filter type
     * @return true if delete success
     * @throws InvalidException invalid
     */
    <F extends IFilter> boolean deleteModel(I id, F data) throws InvalidException;

    /**
     * Check exist
     *
     * @param id     id
     * @param filter filter
     * @param <F>    filter type
     * @return true if exist
     * @throws InvalidException invalid
     */
    <F extends IFilter> boolean existByFilter(I id, F filter) throws InvalidException;

    /**
     * Check exist
     *
     * @param detail detail
     * @return true if exist
     * @throws InvalidException invalid
     */
    boolean existByDetail(U detail) throws InvalidException;

    /**
     * Get detail model
     *
     * @param id     id
     * @param filter filter
     * @param <F>    filter type
     * @return detail model
     * @throws InvalidException invalid
     */
    <F extends IFilter> U getDetailModel(I id, F filter) throws InvalidException;

    /**
     * Get info model
     *
     * @param id     id
     * @param filter filter
     * @param <F>    filter type
     * @return detail model
     * @throws InvalidException invalid
     */
    <F extends IFilter> T getInfoModel(I id, F filter) throws InvalidException;
}
