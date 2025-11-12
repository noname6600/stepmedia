/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.exception;

import io.grpc.Status;
import org.springframework.http.HttpStatus;

/**
 * Mapping between grpc status and http status
 */
public enum StatusMapping {
    /**
     * Ok
     */
    OK(Status.OK, HttpStatus.OK),
    /**
     * Bad request
     */
    BAD_REQUEST(Status.INVALID_ARGUMENT, HttpStatus.BAD_REQUEST),
    /**
     * Request timeout
     */
    REQUEST_TIMEOUT(Status.DEADLINE_EXCEEDED, HttpStatus.REQUEST_TIMEOUT),
    /**
     * Not found
     */
    NOT_FOUND(Status.NOT_FOUND, HttpStatus.BAD_REQUEST),
    /**
     * Permission denied
     */
    PERMISSION_DENIED(Status.PERMISSION_DENIED, HttpStatus.FORBIDDEN),
    /**
     * Unimplemented
     */
    UNIMPLEMENTED(Status.UNAVAILABLE, HttpStatus.NOT_FOUND),
    /**
     * Internal server error
     */
    INTERNAL_SERVER_ERROR(Status.INTERNAL, HttpStatus.INTERNAL_SERVER_ERROR),
    /**
     * Service unavailable
     */
    UNAVAILABLE(Status.UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE),
    /**
     * Unauthorized
     */
    UNAUTHORIZED(Status.UNAUTHENTICATED, HttpStatus.UNAUTHORIZED),
    /**
     * Conflict
     */
    CONFLICT(Status.INVALID_ARGUMENT, HttpStatus.CONFLICT),

    /**
     * Already exists
     */
    ALREADY_EXISTS(Status.ALREADY_EXISTS, HttpStatus.CONFLICT),
    /**
     * Payment required
     */
    PAYMENT_REQUIRED(Status.INVALID_ARGUMENT, HttpStatus.PAYMENT_REQUIRED),
    /**
     * method not allowed
     */
    METHOD_NOT_ALLOWED(Status.INVALID_ARGUMENT, HttpStatus.METHOD_NOT_ALLOWED);
    private final Status status;
    private final HttpStatus httpStatus;

    /**
     * Status mapping
     *
     * @param status     grpc status
     * @param httpStatus http status
     */
    StatusMapping(Status status, HttpStatus httpStatus) {
        this.status = status;
        this.httpStatus = httpStatus;
    }

    /**
     * Get grpc status
     *
     * @return grpc status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Get http status
     *
     * @return http status
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
