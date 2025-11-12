package com.thanhvh.exception;

/**
 * Interface for error code
 */
public interface IErrorCode {

    /**
     * Gets status mapping.
     *
     * @return Status mapping
     */
    StatusMapping getStatusMapping();

    /**
     * Gets code.
     *
     * @return Error code
     */
    int getCode();

    /**
     * Name string.
     *
     * @return enum name
     */
    String name();

    /**
     * Gets title message code.
     *
     * @return the title message code
     */
    default String getTitleMessageCode() {
        return "problemDetail." + this.getClass().getName() + "." + getCode();
    }

    /**
     * Gets detail message code.
     *
     * @return the detail message code
     */
    default String getDetailMessageCode() {
        return "problemDetail.title." + this.getClass().getName();
    }

}
