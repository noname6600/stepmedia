package com.thanhvh.shopmanagement.modules.order.exception;

import com.thanhvh.exception.IErrorCode;
import com.thanhvh.exception.StatusMapping;
import lombok.Getter;

@Getter
public enum OrderError implements IErrorCode {
    ORDER_NOT_FOUND(StatusMapping.NOT_FOUND),
    DELIVERY_DATE_INVALID(StatusMapping.BAD_REQUEST),
    ;

    private final StatusMapping statusMapping;
    private final int code;

    OrderError(StatusMapping statusMapping) {
        this.statusMapping = statusMapping;
        this.code = ordinal();
    }
}
