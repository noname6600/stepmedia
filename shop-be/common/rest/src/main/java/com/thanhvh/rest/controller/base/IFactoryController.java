/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.controller.base;

import com.thanhvh.rest.factory.data.IFactory;
import com.thanhvh.rest.factory.response.IResponseFactory;
import com.thanhvh.rest.model.IBaseData;

/**
 * Base factory controller
 *
 * @param <I> id type
 * @param <T> info type
 * @param <U> detail type
 */
public interface IFactoryController<I, T extends IBaseData<I>, U extends T> {

    /**
     * Get response factory
     *
     * @return {@link IResponseFactory}
     */
    IResponseFactory getResponseFactory();

    /**
     * Get data factory
     *
     * @return {@link IFactory}
     */
    IFactory<I, T, U> getDataFactory();
}
