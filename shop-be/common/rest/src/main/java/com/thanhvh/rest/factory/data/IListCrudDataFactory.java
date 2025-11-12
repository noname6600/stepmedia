/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.factory.data;

import com.thanhvh.exception.InvalidException;
import com.thanhvh.rest.filter.IFilter;
import com.thanhvh.rest.model.IBaseData;

import java.util.ArrayList;
import java.util.List;

/**
 * List crud factory
 *
 * @param <I> id type
 * @param <T> info type
 * @param <U> detail type
 */
public interface IListCrudDataFactory<I, T extends IBaseData<I>, U extends T> extends ICrudDataFactory<I, T, U> {

    /**
     * Get list info
     *
     * @param filter filter
     * @param <F>    filter type
     * @return List info
     * @throws InvalidException invalid
     */
    <F extends IFilter> List<T> getInfoList(F filter) throws InvalidException;

    /**
     * Get list info
     *
     * @return List info
     * @throws InvalidException invalid
     */
    List<T> getInfoList() throws InvalidException;

    /**
     * Get list detail
     *
     * @param filter filter
     * @param <F>    filter type
     * @return List info
     * @throws InvalidException invalid
     */
    <F extends IFilter> List<U> getDetailList(F filter) throws InvalidException;

    /**
     * Get list detail
     *
     * @return List detail
     * @throws InvalidException invalid
     */
    List<U> getDetailList() throws InvalidException;

    /**
     * Create list
     *
     * @param models list model detail
     * @return list model detail
     * @throws InvalidException invalid
     */
    default List<U> createModels(List<U> models) throws InvalidException {
        List<U> response = new ArrayList<>();
        if (models != null) {
            for (U model : models) {
                createModel(model);
            }
        }
        return response;
    }

    /**
     * Update list
     *
     * @param models list model detail
     * @return list model detail
     * @throws InvalidException invalid
     */
    default List<U> updateModels(List<U> models) throws InvalidException {
        List<U> response = new ArrayList<>();
        if (models != null) {
            for (U model : models) {
                updateModel(model);
            }
        }
        return response;
    }


    /**
     * Update list
     *
     * @param ids list model id
     * @throws InvalidException invalid
     */
    default void deleteList(List<I> ids) throws InvalidException {
        if (ids != null) {
            for (I id : ids) {
                deleteModel(id, null);
            }
        }
    }
}
