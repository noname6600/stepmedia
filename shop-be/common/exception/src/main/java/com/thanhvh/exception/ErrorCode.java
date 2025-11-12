package com.thanhvh.exception;

/**
 * Common error code
 */
public enum ErrorCode implements IErrorCode {
    /**
     * OK
     */
    OK(StatusMapping.OK, 2000000, "problemDetail.errorCode.OK.detail", "problemDetail.errorCode.OK.title"),

    /**
     * Server Error
     */
    SERVER_ERROR(StatusMapping.INTERNAL_SERVER_ERROR, 5000000, "problemDetail.errorCode.SERVER_ERROR.detail", "problemDetail.errorCode.SERVER_ERROR.title"),
    /**
     * Not found token provider
     */
    TOKEN_PROVIDER_NOT_FOUND(StatusMapping.INTERNAL_SERVER_ERROR, 50000001, "problemDetail.errorCode.TOKEN_PROVIDER_NOT_FOUND.detail", "problemDetail.errorCode.TOKEN_PROVIDER_NOT_FOUND.title"),
    /**
     * Unsupported method
     */
    UNSUPPORTED_METHOD(StatusMapping.UNIMPLEMENTED, 5000002, "problemDetail.errorCode.UNSUPPORTED_METHOD.detail", "problemDetail.errorCode.UNSUPPORTED_METHOD.title"),
    /**
     * Model id type not same entity id type
     */
    CAN_NOT_CAST_ID_ERROR(StatusMapping.INTERNAL_SERVER_ERROR, 5000003, "problemDetail.errorCode.CAN_NOT_CAST_ID_ERROR.detail", "problemDetail.errorCode.CAN_NOT_CAST_ID_ERROR.title"),
    /**
     * Duplication assignment factory
     */
    DUPLICATE_SAME_ASSIGNMENT_FACTORY(StatusMapping.INTERNAL_SERVER_ERROR, 5000004, "problemDetail.errorCode.DUPLICATE_SAME_ASSIGNMENT_FACTORY.detail", "problemDetail.errorCode.DUPLICATE_SAME_ASSIGNMENT_FACTORY.title"),
    /**
     * Get cache error
     */
    GET_CACHE_ERROR(StatusMapping.INTERNAL_SERVER_ERROR, 5000005, "problemDetail.errorCode.GET_CACHE_ERROR.detail", "problemDetail.errorCode.GET_CACHE_ERROR.title"),

    /**
     * CLEAR_CACHE_ERROR
     */
    CLEAR_CACHE_ERROR(StatusMapping.INTERNAL_SERVER_ERROR, 5000006, "problemDetail.errorCode.CLEAR_CACHE_ERROR.detail", "problemDetail.errorCode.CLEAR_CACHE_ERROR.title"),

    /**
     * Unauthorized
     */
    UNAUTHORIZED(StatusMapping.UNAUTHORIZED, 4010000, "problemDetail.errorCode.UNAUTHORIZED.detail", "problemDetail.errorCode.UNAUTHORIZED.title"),
    /**
     * Invalid Token
     */
    INVALID_TOKEN(StatusMapping.UNAUTHORIZED, 4010001, "problemDetail.errorCode.INVALID_TOKEN.detail", "problemDetail.errorCode.INVALID_TOKEN.title"),
    /**
     * Invalid X-API-KEY
     */
    INVALID_X_API_KEY(StatusMapping.UNAUTHORIZED, 4010002, "problemDetail.errorCode.INVALID_X_API_KEY.detail", "problemDetail.errorCode.INVALID_X_API_KEY.title"),

    /**
     * Not Found
     */
    NOT_FOUND(StatusMapping.BAD_REQUEST, 40000002, "problemDetail.errorCode.NOT_FOUND.detail", "problemDetail.errorCode.NOT_FOUND.title"),

    /**
     * Conflict
     */
    CONFLICT(StatusMapping.CONFLICT, 4090000, "problemDetail.errorCode.CONFLICT.detail", "problemDetail.errorCode.CONFLICT.title"),

    /**
     * Invalid date
     */
    INVALID_DATE_TYPE(StatusMapping.CONFLICT, 40900001, "problemDetail.errorCode.INVALID_DATE_TYPE.detail", "problemDetail.errorCode.INVALID_DATE_TYPE.title"),
    /**
     * Convert error
     */
    CONVERT_TO_STRING_ERROR(StatusMapping.CONFLICT, 4090002, "problemDetail.errorCode.CONVERT_TO_STRING_ERROR.detail", "problemDetail.errorCode.CONVERT_TO_STRING_ERROR.title"),

    /**
     * Bad Request
     */
    BAD_REQUEST(StatusMapping.BAD_REQUEST, 40000000, "problemDetail.errorCode.BAD_REQUEST.detail", "problemDetail.errorCode.BAD_REQUEST.title"),

    /**
     * Internal bad Request
     */
    INTERNAL_BAD_REQUEST(StatusMapping.BAD_REQUEST, 40000001, "problemDetail.errorCode.INTERNAL_BAD_REQUEST.detail", "problemDetail.errorCode.INTERNAL_BAD_REQUEST.title");

    private final StatusMapping statusMapping;
    private final int code;

    private final String detailMessageCode;
    private final String titleMessageCode;

    ErrorCode(
            StatusMapping statusMapping,
            int code,
            String detailMessageCode,
            String titleMessageCode
    ) {
        this.statusMapping = statusMapping;
        this.code = code;
        this.detailMessageCode = detailMessageCode;
        this.titleMessageCode = titleMessageCode;
    }

    @Override
    public StatusMapping getStatusMapping() {
        return statusMapping;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDetailMessageCode() {
        return detailMessageCode;
    }

    @Override
    public String getTitleMessageCode() {
        return titleMessageCode;
    }
}
