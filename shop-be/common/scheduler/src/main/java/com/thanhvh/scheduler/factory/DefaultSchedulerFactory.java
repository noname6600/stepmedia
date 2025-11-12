package com.thanhvh.scheduler.factory;

import com.thanhvh.exception.InvalidException;
import com.thanhvh.scheduler.constant.JobControlEnum;
import com.thanhvh.scheduler.exception.ScheduleErrorCode;
import com.thanhvh.scheduler.model.TimeTrigger;
import com.thanhvh.scheduler.service.IJobService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;

import java.util.Set;

/**
 * DefaultSchedulerFactory
 */
public class DefaultSchedulerFactory implements ISchedulerFactory {
    private final IJobService iJobService;

    /**
     * DefaultSchedulerFactory
     *
     * @param iJobService IJobService
     */
    public DefaultSchedulerFactory(IJobService iJobService) {
        this.iJobService = iJobService;

    }

    @Override
    public boolean controlJob(String jobName, String jobGroup, JobControlEnum jobControlEnum) throws InvalidException {
        JobDetail jobDetail = iJobService.getJobDetail(jobName, jobGroup);
        if (jobDetail == null) {
            throw new InvalidException(ScheduleErrorCode.JOB_NOT_FOUND);
        }
        return jobControlEnum.handle(jobName, jobGroup, iJobService);
    }

    @Override
    public boolean unScheduleJob(String jobName, String jobGroup, String triggerId) throws InvalidException {
        JobDetail jobDetail = iJobService.getJobDetail(jobName, jobGroup);
        if (jobDetail == null) {
            throw new InvalidException(ScheduleErrorCode.JOB_NOT_FOUND);
        }
        return iJobService.unScheduleJob(triggerId, jobGroup);
    }

    @Override
    public boolean scheduleJob(String jobName, String jobGroup, Class<? extends Job> jobClass, Set<TimeTrigger> timeTriggers, JobDataMap jobDataMap, String description) throws InvalidException {
        return iJobService.scheduleJob(
                jobName,
                jobGroup,
                jobClass,
                timeTriggers,
                jobDataMap,
                description
        );
    }

    @Override
    public boolean scheduleJob(String jobName, String jobGroup, Class<? extends Job> jobClass, Set<TimeTrigger> timeTriggers, JobDataMap jobDataMap, String description, boolean isDurable) throws InvalidException {
        return iJobService.scheduleJob(
                jobName,
                jobGroup,
                jobClass,
                timeTriggers,
                jobDataMap,
                description,
                isDurable
        );
    }

    @Override
    public boolean reScheduleJob(String jobName, String jobGroup, String triggerId, TimeTrigger newTrigger) throws InvalidException {
        JobDetail jobDetail = iJobService.getJobDetail(jobName, jobGroup);
        if (jobDetail == null) {
            throw new InvalidException(ScheduleErrorCode.JOB_NOT_FOUND);
        }
        return iJobService.rescheduleJob(
                jobName,
                jobGroup,
                triggerId,
                jobGroup,
                newTrigger
        );
    }

    @Override
    public JobDetail getJobDetail(String jobName, String jobGroup) throws InvalidException {
        JobDetail jobDetail = iJobService.getJobDetail(jobName, jobGroup);
        if (jobDetail == null) {
            throw new InvalidException(ScheduleErrorCode.JOB_NOT_FOUND);
        }
        return jobDetail;
    }
}
