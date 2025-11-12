/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.model.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Base response
 *
 * @param <T> type data
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class BaseResponse<T> implements ResponseWrapper<T> {
    private boolean success;
    private T data;
}
