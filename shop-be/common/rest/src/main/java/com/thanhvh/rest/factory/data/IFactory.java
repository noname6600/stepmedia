/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.factory.data;


import com.thanhvh.rest.model.IBaseData;

/**
 * Data factory
 *
 * @param <I> id type
 * @param <T> info type
 * @param <U> detail type
 */
public interface IFactory<I, T extends IBaseData<I>, U extends T> {
}
