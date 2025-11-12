/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.controller.base;

import com.thanhvh.exception.InvalidException;
import com.thanhvh.exception.RestException;
import com.thanhvh.rest.factory.data.IPagingDataFactory;
import com.thanhvh.rest.filter.IFilter;
import com.thanhvh.rest.filter.ISearchFilter;
import com.thanhvh.rest.model.IBaseData;
import com.thanhvh.rest.model.response.BasePagingResponse;
import com.thanhvh.rest.model.response.BaseResponse;
import org.springframework.http.ResponseEntity;

/**
 * Paging controller
 *
 * @param <I> id type
 * @param <T> info type
 * @param <U> detail type
 */
public interface IPagingFactoryController<I, T extends IBaseData<I>, U extends T> extends IFactoryController<I, T, U> {

    /**
     * Get PagingDataFactory
     *
     * @return IPagingDataFactory
     */
    IPagingDataFactory<I, T, U> getPageDataFactory();


    /**
     * Paging list info model
     *
     * @param filter extend {@link IFilter}
     * @param number Page number
     * @param size   Page size
     * @param <F>    type of filter
     * @return {@link BasePagingResponse} have paging list info model
     */
    default <F extends IFilter> ResponseEntity<BaseResponse<BasePagingResponse<T>>> factoryGetInfoPageWithFilter(F filter, Integer number, Integer size) {
        try {
            return getResponseFactory().success((getPageDataFactory()).getInfoPage(filter, number, size));
        } catch (InvalidException e) {
            throw RestException.create(e);
        }
    }

    /**
     * Paging list detail model
     *
     * @param filter extend {@link IFilter}
     * @param number Page number
     * @param size   Page size
     * @param <F>    type of filter
     * @return {@link BasePagingResponse} have paging list info model
     */
    default <F extends IFilter> ResponseEntity<BaseResponse<BasePagingResponse<U>>> factoryGetDetailPageWithFilter(F filter, Integer number, Integer size) {
        try {
            return getResponseFactory().success((getPageDataFactory()).getDetailPage(filter, number, size));
        } catch (InvalidException e) {
            throw RestException.create(e);
        }
    }

    /**
     * Paging search info model
     *
     * @param filter extend {@link IFilter}
     * @param number Page number
     * @param size   Page size
     * @param <F>    type of filter
     * @return {@link BasePagingResponse} have paging list info model
     */
    default <F extends ISearchFilter> ResponseEntity<BaseResponse<BasePagingResponse<T>>> factoryGetSearchInfoWithFilter(F filter, Integer number, Integer size) {
        try {
            return getResponseFactory().success((getPageDataFactory()).searchInfo(filter, number, size));
        } catch (InvalidException e) {
            throw RestException.create(e);
        }
    }

    /**
     * Paging search info model
     *
     * @param filter extend {@link IFilter}
     * @param number Page number
     * @param size   Page size
     * @param <F>    type of filter
     * @return {@link BasePagingResponse} have paging list info model
     */
    default <F extends ISearchFilter> ResponseEntity<BaseResponse<BasePagingResponse<U>>> factoryGetSearchDetailWithFilter(F filter, Integer number, Integer size) {
        try {
            return getResponseFactory().success((getPageDataFactory()).searchDetail(filter, number, size));
        } catch (InvalidException e) {
            throw RestException.create(e);
        }
    }

}
