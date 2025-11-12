/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.controller.base;

import com.thanhvh.rest.factory.data.ICrudDataFactory;
import com.thanhvh.rest.factory.data.IDataFactory;
import com.thanhvh.rest.factory.data.IListCrudDataFactory;
import com.thanhvh.rest.factory.data.IPagingDataFactory;
import com.thanhvh.rest.factory.response.IResponseFactory;
import com.thanhvh.rest.model.IBaseData;

/**
 * Base controller
 *
 * @param <I> id
 * @param <T> info model
 * @param <U> detail model
 */
public abstract class BaseController<I, T extends IBaseData<I>, U extends T> implements
        IPagingFactoryController<I, T, U>,
        IListFactoryController<I, T, U>,
        ICrudFactoryController<I, T, U> {

    /**
     * Response factory
     */
    protected IResponseFactory iResponseFactory;

    /**
     * Data factory
     */
    protected IDataFactory<I, T, U> iDataFactory;

    /**
     * @param iResponseFactory response factory
     * @param iDataFactory     data factory
     */
    protected BaseController(IResponseFactory iResponseFactory, IDataFactory<I, T, U> iDataFactory) {
        this.iResponseFactory = iResponseFactory;
        this.iDataFactory = iDataFactory;
    }

    /**
     * Get IResponseFactory
     *
     * @return response factory
     */
    @Override
    public IResponseFactory getResponseFactory() {
        return iResponseFactory;
    }

    /**
     * Get IDataFactory
     *
     * @return data factory
     */
    @Override
    public IDataFactory<I, T, U> getDataFactory() {
        return iDataFactory;
    }

    /**
     * Get ICrudDataFactory
     *
     * @return data factory
     */
    @Override
    public ICrudDataFactory<I, T, U> getCrudDataFactory() {
        return iDataFactory;
    }

    /**
     * Get IListCrudDataFactory
     *
     * @return data factory
     */
    @Override
    public IListCrudDataFactory<I, T, U> getListFactory() {
        return iDataFactory;
    }

    /**
     * Get IPagingDataFactory
     *
     * @return data factory
     */
    @Override
    public IPagingDataFactory<I, T, U> getPageDataFactory() {
        return iDataFactory;
    }
}
