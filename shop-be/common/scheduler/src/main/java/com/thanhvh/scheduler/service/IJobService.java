package com.thanhvh.scheduler.service;

import com.thanhvh.exception.InvalidException;
import com.thanhvh.scheduler.constant.TriggerState;
import com.thanhvh.scheduler.model.TimeTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.util.List;
import java.util.Set;

/**
 * IJobService
 */
public interface IJobService {
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
     * rescheduleJob
     *
     * @param jobName      jobName
     * @param jobGroup     jobGroup
     * @param triggerName  triggerName
     * @param triggerGroup triggerGroup
     * @param newTrigger   newTrigger
     * @return boolean
     * @throws InvalidException InvalidException
     */
    boolean rescheduleJob(String jobName,
                          String jobGroup,
                          String triggerName,
                          String triggerGroup,
                          TimeTrigger newTrigger) throws InvalidException;

    /**
     * unScheduleJob
     *
     * @param triggerName  triggerName
     * @param triggerGroup triggerGroup
     * @return boolean
     */
    boolean unScheduleJob(String triggerName, String triggerGroup);

    /**
     * deleteJob
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return boolean
     */
    boolean deleteJob(String jobName, String jobGroup);

    /**
     * pauseJob
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return boolean
     */
    boolean pauseJob(String jobName, String jobGroup);

    /**
     * resumeJob
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return boolean
     */
    boolean resumeJob(String jobName, String jobGroup);

    /**
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return boolean
     */
    boolean startJobNow(String jobName, String jobGroup);

    /**
     * isJobRunning
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return boolean
     */
    boolean isJobRunning(String jobName, String jobGroup);

    /**
     * getAllJobsByGroup
     *
     * @param jobGroup jobGroup
     * @return JobDetail
     * @throws InvalidException InvalidException
     */
    List<JobDetail> getAllJobsByGroup(String jobGroup) throws InvalidException;

    /**
     * isJobWithNamePresent
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return boolean
     */
    boolean isJobWithNamePresent(String jobName, String jobGroup);

    /**
     * getTriggerState
     *
     * @param triggerName  triggerName
     * @param triggerGroup triggerGroup
     * @return TriggerState
     */
    TriggerState getTriggerState(String triggerName, String triggerGroup);

    /**
     * getJobDetail
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return JobDetail
     */
    JobDetail getJobDetail(String jobName, String jobGroup);

    /**
     * getTriggersOfJob
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return Trigger
     * @throws InvalidException InvalidException
     */
    List<Trigger> getTriggersOfJob(String jobName, String jobGroup) throws InvalidException;

    /**
     * stopJob
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return boolean
     */
    boolean stopJob(String jobName, String jobGroup);
}
