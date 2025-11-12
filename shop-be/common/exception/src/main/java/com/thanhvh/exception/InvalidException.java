package com.thanhvh.exception;

import org.springframework.lang.Nullable;

/**
 * Implement CustomException and Exception
 * <br>
 * Often using in logic
 */
public class InvalidException extends Exception implements CustomException {
    /**
     * Error code
     */
    private final transient IErrorCode errorCode;
    /**
     * Args
     */
    private final Object[] messageDetailArguments;

    /**
     * @param errorCode implement of IErrorCode
     */
    public InvalidException(IErrorCode errorCode) {
        this(errorCode, null, null);
    }

    /**
     * @param errorCode error code
     * @param cause     cause
     */
    public InvalidException(IErrorCode errorCode, Throwable cause) {
        this(errorCode, null, cause);
    }


    /**
     * @param errorCode              error code
     * @param messageDetailArguments an array of arguments that will
     *                               be filled in for params within
     *                               the message (params look like "{0}", "{1,date}",
     *                               "{2,time}" within a message),
     *                               or null if none
     * @param cause                  cause
     */
    public InvalidException(IErrorCode errorCode, @Nullable Object[] messageDetailArguments, Throwable cause) {
        super(null, cause);
        this.errorCode = errorCode;
        this.messageDetailArguments = messageDetailArguments;
    }

    @Override
    public IErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public Object[] getDetailMessageArguments() {
        return messageDetailArguments;
    }
}
