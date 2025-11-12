package com.thanhvh.rest.model.response;

import com.thanhvh.exception.InvalidException;

/**
 * Response wrapper
 *
 * @param <T> data type
 */
public interface ResponseWrapper<T> {
    /**
     * Data
     *
     * @return data
     */
    T getData();

    /**
     * Success
     *
     * @return success
     * @throws InvalidException invalid
     */
    boolean isSuccess() throws InvalidException;
}
