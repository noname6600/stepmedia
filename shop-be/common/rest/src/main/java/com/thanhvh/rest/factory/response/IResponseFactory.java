/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.factory.response;


import com.thanhvh.rest.model.response.BaseResponse;
import org.springframework.http.ResponseEntity;


/**
 * Factory create response entity
 */
public interface IResponseFactory {
    /**
     * Success
     *
     * @param data data
     * @param <I>  data type
     * @return {@link ResponseEntity}
     */
    <I> ResponseEntity<BaseResponse<I>> success(I data);
}
