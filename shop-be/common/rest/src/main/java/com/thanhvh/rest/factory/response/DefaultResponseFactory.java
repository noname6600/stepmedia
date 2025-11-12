/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.factory.response;

import com.thanhvh.rest.model.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * Default response factory
 */
public class DefaultResponseFactory implements IResponseFactory {

    @Override
    public <I> ResponseEntity<BaseResponse<I>> success(I data) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        BaseResponse.
                                <I>builder()
                                .data(data)
                                .success(true)
                                .build()
                );
    }
}
