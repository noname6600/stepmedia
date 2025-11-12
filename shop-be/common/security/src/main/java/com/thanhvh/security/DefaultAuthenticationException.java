package com.thanhvh.security;


import com.thanhvh.exception.CustomException;
import com.thanhvh.exception.IErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

import java.util.HashMap;
import java.util.Map;

/**
 * DefaultAuthenticationException
 *
 * @see AuthenticationException
 * @see CustomException
 */
@Getter
public class DefaultAuthenticationException extends AuthenticationException implements CustomException {
    /**
     * Error code
     */
    private final transient IErrorCode iErrorCode;
    /**
     * Args
     */
    private final Map<String, String> args;

    /**
     * @param errorCode {@link IErrorCode}
     */
    public DefaultAuthenticationException(IErrorCode errorCode) {
        super(errorCode.name());
        this.iErrorCode = errorCode;
        this.args = new HashMap<>();
    }

    @Override
    public IErrorCode getErrorCode() {
        return this.iErrorCode;
    }
}
