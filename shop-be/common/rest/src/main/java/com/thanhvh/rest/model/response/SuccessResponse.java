/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.model.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Success response
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class SuccessResponse {
    /**
     * The constant INSTANCE.
     */
    public static final SuccessResponse INSTANCE = SuccessResponse.builder().success(true).build();
    private boolean success;
}
