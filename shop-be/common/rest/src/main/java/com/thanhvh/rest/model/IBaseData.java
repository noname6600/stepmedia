/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.model;

import java.io.Serializable;

/**
 * Base data model
 *
 * @param <T> id type
 */
public interface IBaseData<T> extends Serializable {
    /**
     * Get id
     *
     * @return id
     */
    T getId();
}
