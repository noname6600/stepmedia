/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.exception;

import io.grpc.Status;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;

/**
 * Grpc Exception
 * <br>
 * Often using in last step GRPC
 */
public class GrpcException extends RuntimeException implements CustomException {
    /**
     * Error code
     */
    private final transient IErrorCode errorCode;

    /**
     * Using default ErrorCode message or not
     */
    private final Object[] messageDetailArguments;

    /**
     * GrpcException constructor with IErrorCode
     *
     * @param errorCode implement of IErrorCode
     */
    public GrpcException(IErrorCode errorCode) {
        this(null, errorCode);
    }

    /**
     * GrpcException constructor with IErrorCode
     *
     * @param messageSource the message source
     * @param errorCode     implement of IErrorCode
     */
    public GrpcException(MessageSource messageSource, IErrorCode errorCode) {
        this(null, errorCode, null, null);
    }

    /**
     * GrpcException constructor with CustomException
     *
     * @param customException implement of CustomException
     */
    public GrpcException(CustomException customException) {
        this(
                null,
                customException
        );
    }

    /**
     * GrpcException constructor with CustomException
     *
     * @param messageSource   the message source
     * @param customException implement of CustomException
     */
    public GrpcException(MessageSource messageSource, CustomException customException) {
        this(
                messageSource,
                customException.getErrorCode(),
                customException.getDetailMessageArguments(),
                customException.getCause() != null
                        ? customException.getCause()
                        : (Throwable) customException
        );
    }

    /**
     * Instantiates a new Grpc exception.
     *
     * @param messageSource          the message source
     * @param errorCode              implement of IErrorCode
     * @param messageDetailArguments args
     * @param cause                  cause
     */
    public GrpcException(MessageSource messageSource, IErrorCode errorCode, @Nullable Object[] messageDetailArguments, Throwable cause) {
        super(getMessage(messageSource, errorCode, messageDetailArguments), cause);
        this.errorCode = errorCode;
        this.messageDetailArguments = messageDetailArguments;
    }

    private static String getMessage(MessageSource messageSource, IErrorCode errorCode, @Nullable Object[] messageDetailArguments) {
        if (messageSource == null) {
            return errorCode.getDetailMessageCode();
        }
        return messageSource.getMessage(errorCode.getDetailMessageCode(), messageDetailArguments, LocaleContextHolder.getLocale());
    }

    @Override
    public IErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * Get grpc status
     *
     * @return grpc status
     */
    public Status getStatus() {
        return errorCode.getStatusMapping().getStatus().withDescription(errorCode.name());
    }

    @Override
    public Object[] getDetailMessageArguments() {
        return messageDetailArguments;
    }
}
