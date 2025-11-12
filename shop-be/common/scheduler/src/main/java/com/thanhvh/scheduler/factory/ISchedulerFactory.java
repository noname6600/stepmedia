package com.thanhvh.scheduler.factory;

import com.thanhvh.exception.InvalidException;
import com.thanhvh.scheduler.constant.JobControlEnum;
import com.thanhvh.scheduler.model.TimeTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;

import java.util.Set;

/**
 * ISchedulerFactory
 */
public interface ISchedulerFactory {
    /**
     * controlJob
     *
     * @param jobName        jobName
     * @param jobGroup       jobGroup
     * @param jobControlEnum jobControlEnum
     * @return boolean
     * @throws InvalidException InvalidException
     */
    boolean controlJob(String jobName, String jobGroup, JobControlEnum jobControlEnum) throws InvalidException;

    /**
     * unScheduleJob
     *
     * @param jobName   jobName
     * @param jobGroup  jobGroup
     * @param triggerId triggerId
     * @return boolean
     * @throws InvalidException InvalidException
     */
    boolean unScheduleJob(String jobName, String jobGroup, String triggerId) throws InvalidException;

    /**
     * scheduleJob
     *
     * @param jobName      jobName
     * @param jobGroup     jobGroup
     * @param jobClass     jobClass
     * @param timeTriggers timeTriggers
     * @param jobDataMap   jobDataMap
     * @param description  description
     * @return boolean
     * @throws InvalidException InvalidException
     */
    boolean scheduleJob(String jobName,
                        String jobGroup,
                        Class<? extends Job> jobClass,
                        Set<TimeTrigger> timeTriggers,
                        JobDataMap jobDataMap,
                        String description) throws InvalidException;

    /**
     * scheduleJob
     *
     * @param jobName      jobName
     * @param jobGroup     jobGroup
     * @param jobClass     jobClass
     * @param timeTriggers timeTriggers
     * @param jobDataMap   jobDataMap
     * @param description  description
     * @param isDurable    isDurable
     * @return boolean
     * @throws InvalidException InvalidException
     */
    boolean scheduleJob(String jobName,
                        String jobGroup,
                        Class<? extends Job> jobClass,
                        Set<TimeTrigger> timeTriggers,
                        JobDataMap jobDataMap,
                        String description,
                        boolean isDurable) throws InvalidException;

    /**
     * reScheduleJob
     *
     * @param jobName    jobName
     * @param jobGroup   jobGroup
     * @param triggerId  triggerId
     * @param newTrigger newTrigger
     * @return boolean
     * @throws InvalidException InvalidException
     */
    boolean reScheduleJob(String jobName, String jobGroup, String triggerId, TimeTrigger newTrigger) throws InvalidException;

    /**
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return JobDetail
     * @throws InvalidException InvalidException
     */
    JobDetail getJobDetail(String jobName, String jobGroup) throws InvalidException;
}
