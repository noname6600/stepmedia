package com.thanhvh.scheduler.exception;

import com.thanhvh.exception.IErrorCode;
import com.thanhvh.exception.StatusMapping;
import lombok.Getter;

/**
 * ScheduleErrorCode
 */
@Getter
public enum ScheduleErrorCode implements IErrorCode {

    /**
     * JOB_PAUSE_FAILED
     */
    JOB_PAUSE_FAILED(StatusMapping.BAD_REQUEST, 400001),
    /**
     * JOB_IS_RUNNING_CANT_DELETE
     */
    JOB_IS_RUNNING_CANT_DELETE(StatusMapping.BAD_REQUEST, 400002),
    /**
     * JOB_NOT_FOUND
     */
    JOB_NOT_FOUND(StatusMapping.NOT_FOUND, 400003),
    /**
     * JOB_STOP_FAILED
     */
    JOB_STOP_FAILED(StatusMapping.BAD_REQUEST, 400004),
    /**
     * JOB_START_FAILED
     */
    JOB_START_FAILED(StatusMapping.BAD_REQUEST, 400005),
    /**
     * JOB_RESUME_FAILED
     */
    JOB_RESUME_FAILED(StatusMapping.BAD_REQUEST, 400006),
    /**
     * JOB_UNSCHEDULED_FAILED
     */
    JOB_UNSCHEDULED_FAILED(StatusMapping.BAD_REQUEST, 400007),
    /**
     * JOB_IS_RUNNING
     */
    JOB_IS_RUNNING(StatusMapping.BAD_REQUEST, 400008),
    /**
     * JOB_DELETE_FAILED
     */
    JOB_DELETE_FAILED(StatusMapping.BAD_REQUEST, 400009),

    /**
     * JOB_TRIGGER_GET_ERROR
     */
    JOB_TRIGGER_GET_ERROR(StatusMapping.BAD_REQUEST, 400010),
    /**
     * JOB_ALREADY_EXISTED
     */
    JOB_ALREADY_EXISTED(StatusMapping.CONFLICT, 400011),
    /**
     * CRON_EXPRESSION_INVALID
     */
    CRON_EXPRESSION_INVALID(StatusMapping.BAD_REQUEST, 400012),
    ;

    private final StatusMapping statusMapping;
    private final int code;

    ScheduleErrorCode(StatusMapping statusMapping, int code) {
        this.statusMapping = statusMapping;
        this.code = code;
    }
}
