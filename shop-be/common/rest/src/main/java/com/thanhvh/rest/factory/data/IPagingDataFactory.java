/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.factory.data;

import com.thanhvh.exception.InvalidException;
import com.thanhvh.rest.filter.IFilter;
import com.thanhvh.rest.filter.ISearchFilter;
import com.thanhvh.rest.model.IBaseData;
import com.thanhvh.rest.model.response.BasePagingResponse;

/**
 * Paging controller
 *
 * @param <I> id type
 * @param <T> info type
 * @param <U> detail type
 */
public interface IPagingDataFactory<I, T extends IBaseData<I>, U extends T> extends IFactory<I, T, U> {

    /**
     * Get page info
     *
     * @param filter filter
     * @param number number of page
     * @param size   number of size
     * @param <F>    filter type
     * @return Page info
     * @throws InvalidException invalid
     */
    <F extends IFilter> BasePagingResponse<T> getInfoPage(F filter, Integer number, Integer size) throws InvalidException;


    /**
     * Get page detail
     *
     * @param filter filter
     * @param number number of page
     * @param size   number of size
     * @param <F>    filter type
     * @return Page info
     * @throws InvalidException invalid
     */
    <F extends IFilter> BasePagingResponse<U> getDetailPage(F filter, Integer number, Integer size) throws InvalidException;

    /**
     * search info
     *
     * @param filter filter
     * @param number number
     * @param size   size
     * @param <F>    type of filter
     * @return BasePagingResponse info model
     * @throws InvalidException ex
     */
    <F extends ISearchFilter> BasePagingResponse<T> searchInfo(F filter, Integer number, Integer size) throws InvalidException;

    /**
     * search detail
     *
     * @param filter filter
     * @param number number
     * @param size   size
     * @param <F>    type of filter
     * @return BasePagingResponse detail model
     * @throws InvalidException ex
     */
    <F extends ISearchFilter> BasePagingResponse<U> searchDetail(F filter, Integer number, Integer size) throws InvalidException;

}
