/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.controller.base;

import com.thanhvh.exception.InvalidException;
import com.thanhvh.exception.RestException;
import com.thanhvh.rest.factory.data.IListCrudDataFactory;
import com.thanhvh.rest.filter.IFilter;
import com.thanhvh.rest.model.IBaseData;
import com.thanhvh.rest.model.response.BaseResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * List controller
 *
 * @param <I> id type
 * @param <T> info type
 * @param <U> detail type
 */
public interface IListFactoryController<I, T extends IBaseData<I>, U extends T> extends IFactoryController<I, T, U> {

    /**
     * Get ListCrudDataFactory
     *
     * @return IListCrudDataFactory
     */
    IListCrudDataFactory<I, T, U> getListFactory();


    /**
     * Return list info model with filter
     *
     * @param filter extend {@link IFilter}\
     * @param <F>    type of filter
     * @return list of info model
     */
    default <F extends IFilter> ResponseEntity<BaseResponse<List<T>>> factoryGetInfoListWithFilter(F filter) {
        try {
            return getResponseFactory().success((getListFactory()).getInfoList(filter));
        } catch (InvalidException e) {
            throw RestException.create(e);
        }
    }
}
