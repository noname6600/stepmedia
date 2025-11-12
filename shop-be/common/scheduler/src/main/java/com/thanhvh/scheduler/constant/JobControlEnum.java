package com.thanhvh.scheduler.constant;

import com.thanhvh.exception.IErrorCode;
import com.thanhvh.exception.InvalidException;
import com.thanhvh.scheduler.exception.ScheduleErrorCode;
import com.thanhvh.scheduler.factory.JobControl;
import com.thanhvh.scheduler.service.IJobService;

/**
 * JobControlEnum
 */
public enum JobControlEnum {
    /**
     * DELETE
     */
    DELETE(
            ((jobName, groupKey, iJobService) -> {
                if (!iJobService.isJobWithNamePresent(jobName, groupKey)) {
                    throw new InvalidException(ScheduleErrorCode.JOB_NOT_FOUND);
                }
                if (iJobService.isJobRunning(jobName, groupKey)) {
                    throw new InvalidException(ScheduleErrorCode.JOB_IS_RUNNING);
                }
                return iJobService.deleteJob(jobName, groupKey);
            }),
            ScheduleErrorCode.JOB_DELETE_FAILED
    ),
    /**
     * PAUSE
     */
    PAUSE(
            ((jobName, groupKey, iJobService) -> {
                if (!iJobService.isJobWithNamePresent(jobName, groupKey)) {
                    throw new InvalidException(ScheduleErrorCode.JOB_NOT_FOUND);
                }
                if (iJobService.isJobRunning(jobName, groupKey)) {
                    throw new InvalidException(ScheduleErrorCode.JOB_IS_RUNNING);
                }
                return iJobService.pauseJob(jobName, groupKey);
            }),
            ScheduleErrorCode.JOB_PAUSE_FAILED
    ),
    /**
     * RESUME
     */
    RESUME(
            ((jobName, groupKey, iJobService) -> {
                if (!iJobService.isJobWithNamePresent(jobName, groupKey)) {
                    throw new InvalidException(ScheduleErrorCode.JOB_NOT_FOUND);
                }
                return iJobService.resumeJob(jobName, groupKey);
            }),
            ScheduleErrorCode.JOB_RESUME_FAILED
    ),
    /**
     * START_NOW
     */
    START_NOW(
            ((jobName, groupKey, iJobService) -> {
                if (!iJobService.isJobWithNamePresent(jobName, groupKey)) {
                    throw new InvalidException(ScheduleErrorCode.JOB_NOT_FOUND);
                }
                if (iJobService.isJobRunning(jobName, groupKey)) {
                    throw new InvalidException(ScheduleErrorCode.JOB_IS_RUNNING);
                }
                return iJobService.startJobNow(jobName, groupKey);
            }),
            ScheduleErrorCode.JOB_START_FAILED
    ),
    /**
     * STOP
     */
    STOP(
            ((jobName, groupKey, iJobService) -> {
                if (!iJobService.isJobWithNamePresent(jobName, groupKey)) {
                    throw new InvalidException(ScheduleErrorCode.JOB_NOT_FOUND);
                }
                return iJobService.stopJob(jobName, groupKey);
            }),
            ScheduleErrorCode.JOB_STOP_FAILED
    ),
    /**
     * CHECK_RUNNING
     */
    CHECK_RUNNING(
            ((jobName, groupKey, iJobService) -> {
                if (!iJobService.isJobWithNamePresent(jobName, groupKey)) {
                    throw new InvalidException(ScheduleErrorCode.JOB_NOT_FOUND);
                }
                return iJobService.isJobRunning(jobName, groupKey);
            }),
            null
    );

    private final JobControl handle;
    private final IErrorCode iErrorCode;

    JobControlEnum(JobControl jobControl, IErrorCode iErrorCode) {
        this.handle = jobControl;
        this.iErrorCode = iErrorCode;
    }

    /**
     * handle
     *
     * @param jobName     String
     * @param groupKey    String
     * @param iJobService IJobService
     * @return boolean
     * @throws InvalidException InvalidException
     */
    public boolean handle(String jobName, String groupKey, IJobService iJobService) throws InvalidException {
        boolean success = this.handle.handle(jobName, groupKey, iJobService);
        if (!success) {
            if (this.iErrorCode == null) {
                return false;
            }
            throw new InvalidException(iErrorCode);
        }
        return true;
    }
}
