package com.thanhvh.exception;

import org.springframework.lang.Nullable;

/**
 * Custom Exception Interface
 */
public interface CustomException {

    /**
     * Gets error code.
     *
     * @return Error code
     */
    IErrorCode getErrorCode();

    /**
     * Message have value : Error at {abc}
     *
     * @return Message Error
     */
    default String getErrorMessage() {
        return getErrorCode().name();
    }

    /**
     * Get cause
     *
     * @return throwable cause
     */
    Throwable getCause();

    /**
     * Get detail message arguments object [ ].
     *
     * @return the object [ ]
     */
    @Nullable
    default Object[] getDetailMessageArguments() {
        return null;
    }
}
