/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.factory.data;

import com.thanhvh.rest.model.IBaseData;

/**
 * IDataFactory
 *
 * @param <I> id
 * @param <T> info model
 * @param <U> detail model
 */
public interface IDataFactory<I, T extends IBaseData<I>, U extends T>
        extends IFactory<I, T, U>,
        IListCrudDataFactory<I, T, U>,
        ICrudDataFactory<I, T, U>,
        IPagingDataFactory<I, T, U> {
}
