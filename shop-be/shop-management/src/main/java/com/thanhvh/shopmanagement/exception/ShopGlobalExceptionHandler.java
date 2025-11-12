package com.thanhvh.shopmanagement.exception;

import com.thanhvh.postgresql.exception.PostgresqlExceptionHandler;
import com.thanhvh.rest.factory.response.IResponseFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ShopGlobalExceptionHandler extends PostgresqlExceptionHandler {
    /**
     * GlobalExceptionHandler
     *
     * @param iResponseFactory {@link IResponseFactory}
     */
    protected ShopGlobalExceptionHandler(IResponseFactory iResponseFactory) {
        super(iResponseFactory);
    }
}
