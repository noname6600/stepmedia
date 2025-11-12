package com.thanhvh.postgresql.exception;

import com.thanhvh.exception.IErrorCode;
import com.thanhvh.exception.StatusMapping;

/**
 * Postgresql Error Code
 */
public enum PostgresqlErrorCode implements IErrorCode {

    /**
     * Foreign Key Violation
     */
    FOREIGN_KEY_VIOLATION(
            StatusMapping.BAD_REQUEST,
            23503,
            "problemDetail.postgresqlErrorCode.FOREIGN_KEY_VIOLATION.detail",
            "problemDetail.postgresqlErrorCode.FOREIGN_KEY_VIOLATION.title"
    ),
    /**
     * Bad Datetime Format
     */
    BAD_DATETIME_FORMAT(
            StatusMapping.BAD_REQUEST,
            22007,
            "problemDetail.postgresqlErrorCode.BAD_DATETIME_FORMAT.detail",
            "problemDetail.postgresqlErrorCode.BAD_DATETIME_FORMAT.title"),
    /**
     * Not Null Violation
     */
    NOT_NULL_VIOLATION(
            StatusMapping.BAD_REQUEST,
            23502,
            "problemDetail.postgresqlErrorCode.NOT_NULL_VIOLATION.detail",
            "problemDetail.postgresqlErrorCode.NOT_NULL_VIOLATION.title"
    ),
    /**
     * Unique Violation
     */
    UNIQUE_VIOLATION(
            StatusMapping.BAD_REQUEST,
            23505,
            "problemDetail.postgresqlErrorCode.UNIQUE_VIOLATION.detail",
            "problemDetail.postgresqlErrorCode.UNIQUE_VIOLATION.title"
    ),
    /**
     * Date Type Mismatch
     */
    DATATYPE_MISMATCH(
            StatusMapping.BAD_REQUEST,
            42804,
            "problemDetail.postgresqlErrorCode.DATATYPE_MISMATCH.detail",
            "problemDetail.postgresqlErrorCode.DATATYPE_MISMATCH.title"
    ),
    /**
     * Invalid Parameter Value
     */
    INVALID_PARAMETER_VALUE(
            StatusMapping.BAD_REQUEST,
            22023,
            "problemDetail.postgresqlErrorCode.INVALID_PARAMETER_VALUE.detail",
            "problemDetail.postgresqlErrorCode.INVALID_PARAMETER_VALUE.title"
    );

    private final StatusMapping statusMapping;
    private final int code;

    private final String detailMessageCode;
    private final String titleMessageCode;

    PostgresqlErrorCode(
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
