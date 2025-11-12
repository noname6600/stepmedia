package com.thanhvh.scheduler.service;

import com.thanhvh.exception.InvalidException;
import com.thanhvh.logging.LogContext;
import com.thanhvh.logging.LogType;
import com.thanhvh.scheduler.constant.TriggerState;
import com.thanhvh.scheduler.exception.ScheduleErrorCode;
import com.thanhvh.scheduler.model.TimeTrigger;
import com.thanhvh.scheduler.utils.JobUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.ZoneId;
import java.util.*;

/**
 * JobService
 */
@Slf4j
@SuppressWarnings("unchecked")
@Component
public class JobService implements IJobService {

    private final ApplicationContext applicationContextProvider;

    private final Scheduler scheduler;

    /**
     * @param schedulerFactoryBean       schedulerFactoryBean
     * @param applicationContextProvider applicationContextProvider
     */
    public JobService(SchedulerFactoryBean schedulerFactoryBean, ApplicationContext applicationContextProvider) {
        this.applicationContextProvider = applicationContextProvider;
        this.scheduler = schedulerFactoryBean.getScheduler();
    }

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
    @Override
    public boolean scheduleJob(String jobName,
                               String jobGroup,
                               Class<? extends Job> jobClass,
                               Set<TimeTrigger> timeTriggers,
                               JobDataMap jobDataMap,
                               String description) throws InvalidException {

        return scheduleJob(
                jobName,
                jobGroup,
                jobClass,
                timeTriggers,
                jobDataMap,
                description,
                false
        );
    }

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
    @Override
    public boolean scheduleJob(String jobName,
                               String jobGroup,
                               Class<? extends Job> jobClass,
                               Set<TimeTrigger> timeTriggers,
                               JobDataMap jobDataMap,
                               String description,
                               boolean isDurable) throws InvalidException {
        if (isJobWithNamePresent(jobName, jobGroup)) {
            throw new InvalidException(ScheduleErrorCode.JOB_ALREADY_EXISTED);
        }

        JobDetail jobDetail = JobUtil.createJob(
                jobClass,
                isDurable,
                applicationContextProvider,
                jobName,
                jobGroup,
                jobDataMap,
                description
        );

        return schedule(jobDetail, createTriggers(jobDetail, timeTriggers));
    }

    /**
     * RescheduleJob
     *
     * @param jobName     jobName
     * @param jobGroup    jobGroup
     * @param triggerName triggerId
     * @param newTrigger  newTrigger
     * @return boolean
     * @throws InvalidException InvalidException
     */
    @Override
    public boolean rescheduleJob(String jobName,
                                 String jobGroup,
                                 String triggerName,
                                 String triggerGroup,
                                 TimeTrigger newTrigger) throws InvalidException {
        try {
            JobDetail jobDetail = getJobDetail(jobName, jobGroup);
            if (jobDetail == null) {
                throw new InvalidException(ScheduleErrorCode.JOB_NOT_FOUND);
            }
            Trigger trigger = createTrigger(jobDetail, newTrigger);
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
            Date date = scheduler.rescheduleJob(triggerKey, trigger);
            LogContext.push(LogType.TRACING, "Reschedule JobName:" + jobName + "JobGroup: " + jobGroup + " to: " + date);
            return true;
        } catch (SchedulerException e) {
            LogContext.push(LogType.TRACING, "Reschedule JobName:" + jobName + "JobGroup: " + jobGroup + " failed. Cause: " + e.getMessage());
            return false;
        }
    }

    /**
     * unScheduleJob
     *
     * @param triggerName  triggerName
     * @param triggerGroup triggerGroup
     * @return boolean
     */
    @Override
    public boolean unScheduleJob(String triggerName,
                                 String triggerGroup) {
        TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
        LogContext.push(LogType.TRACING, "UnScheduling job : triggerKey :" + triggerName);
        try {
            boolean status = scheduler.unscheduleJob(triggerKey);
            LogContext.push(
                    LogType.TRACING,
                    "Trigger associated with triggerKey :"
                            + triggerName
                            + " unscheduled with status :"
                            + status
            );
            return status;
        } catch (SchedulerException e) {
            LogContext.push(LogType.TRACING, "SchedulerException while unScheduling job with key :" + triggerName + " message :" + e.getMessage());
            return false;
        }
    }

    /**
     * Delete a Job
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return boolean
     */
    @Override
    public boolean deleteJob(String jobName, String jobGroup) {
        JobKey jkey = new JobKey(jobName, jobGroup);
        LogContext.push(LogType.TRACING, "Deleting job : jobKey :" + jobName);
        try {
            boolean status = scheduler.deleteJob(jkey);
            LogContext.push(
                    LogType.TRACING,
                    "Job with jobKey :"
                            + jobName
                            + " deleted with status :"
                            + status
            );
            return status;
        } catch (SchedulerException e) {
            LogContext.push(
                    LogType.TRACING,
                    "SchedulerException while deleting job with key :"
                            + jobName
                            + " message :"
                            + e.getMessage()
            );
            return false;
        }
    }

    /**
     * PauseJob
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return boolean
     */
    @Override
    public boolean pauseJob(String jobName, String jobGroup) {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        LogContext.push(
                LogType.TRACING,
                "Pausing job : jobKey :"
                        + jobName
                        + ", jobGroup :"
                        + jobGroup);

        try {
            scheduler.pauseJob(jobKey);
            LogContext.push(
                    LogType.TRACING,
                    "Job with jobKey :"
                            + jobName
                            + " paused successfully."
            );
            return true;
        } catch (SchedulerException e) {
            LogContext.push(
                    LogType.TRACING,
                    "SchedulerException while pausing job with key :"
                            + jobName
                            + " message :"
                            + e.getMessage()
            );
            return false;
        }
    }

    /**
     * Resume paused job
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return boolean
     */
    @Override
    public boolean resumeJob(String jobName, String jobGroup) {
        JobKey jKey = new JobKey(jobName, jobGroup);
        LogContext.push(LogType.TRACING, "Resuming job : jobKey :" + jobName);
        try {
            scheduler.resumeJob(jKey);
            LogContext.push(
                    LogType.TRACING,
                    "Job with jobKey :"
                            + jobName
                            + " resumed successfully."
            );
            return true;
        } catch (SchedulerException e) {
            LogContext.push(
                    LogType.TRACING,
                    "SchedulerException while resuming job with key :"
                            + jobName
                            + " message :"
                            + e.getMessage()
            );
            return false;
        }
    }

    /**
     * Start a job now
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return boolean
     */
    @Override
    public boolean startJobNow(String jobName, String jobGroup) {
        JobKey jKey = new JobKey(jobName, jobGroup);
        LogContext.push(LogType.TRACING, "Starting job now : jobKey :" + jobName);
        try {
            scheduler.triggerJob(jKey);
            LogContext.push(
                    LogType.TRACING,
                    "Job with jobKey :"
                            + jobName
                            + " started now successfully."
            );
            return true;
        } catch (SchedulerException e) {
            LogContext.push(
                    LogType.TRACING,
                    "SchedulerException while starting job now with key :"
                            + jobName
                            + " message :"
                            + e.getMessage()
            );
            return false;
        }
    }

    /**
     * Check if job is already running
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return boolean
     */
    @Override
    public boolean isJobRunning(String jobName, String jobGroup) {
        LogContext.push(LogType.TRACING, "Checking job is running now : jobKey :" + jobName);
        try {
            List<JobExecutionContext> currentJobs = scheduler.getCurrentlyExecutingJobs();
            if (currentJobs != null) {
                for (JobExecutionContext jobCtx : currentJobs) {
                    String jobNameDB = jobCtx.getJobDetail().getKey().getName();
                    String groupNameDB = jobCtx.getJobDetail().getKey().getGroup();
                    if (jobName.equalsIgnoreCase(jobNameDB) && jobGroup.equalsIgnoreCase(groupNameDB)) {
                        return true;
                    }
                }
            }
        } catch (SchedulerException e) {
            LogContext.push(
                    LogType.TRACING,
                    "SchedulerException while checking job with key :"
                            + jobName
                            + " is running. error message :"
                            + e.getMessage()
            );
            return false;
        }
        return false;
    }

    /**
     * @param jobGroup jobGroup
     * @return JobDetail
     * @throws InvalidException InvalidException
     */
    @Override
    public List<JobDetail> getAllJobsByGroup(String jobGroup) throws InvalidException {
        try {
            List<JobDetail> jobDetails = new ArrayList<>();
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(jobGroup))) {
                jobDetails.add(scheduler.getJobDetail(jobKey));
            }
            return jobDetails;
        } catch (SchedulerException e) {
            throw new InvalidException(ScheduleErrorCode.JOB_NOT_FOUND);
        }
    }

    /**
     * Check job exist with given name
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return boolean
     */
    @Override
    public boolean isJobWithNamePresent(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            if (scheduler.checkExists(jobKey)) {
                return true;
            }
        } catch (SchedulerException e) {
            LogContext.push(
                    LogType.TRACING,
                    "SchedulerException while checking job with name and group exist:"
                            + e.getMessage()
            );
        }
        return false;
    }

    /**
     * @param triggerName  triggerName
     * @param triggerGroup triggerGroup
     * @return TriggerState
     */
    @Override
    public TriggerState getTriggerState(String triggerName, String triggerGroup) {
        TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
        try {
            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
            return TriggerState.fromState(triggerState);
        } catch (SchedulerException e) {
            return TriggerState.UNKNOWN;
        }
    }

    /**
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return JobDetail
     */
    @Override
    public JobDetail getJobDetail(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            return scheduler.getJobDetail(jobKey);
        } catch (Exception e) {
            LogContext.push(LogType.TRACING, "Job not Existed: " + e.getMessage());
            return null;
        }
    }

    /**
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return Trigger
     * @throws InvalidException InvalidException
     */
    @Override
    public List<Trigger> getTriggersOfJob(String jobName, String jobGroup) throws InvalidException {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            return (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
        } catch (Exception e) {
            throw new InvalidException(ScheduleErrorCode.JOB_TRIGGER_GET_ERROR);
        }
    }

    /**
     * Stop a job
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return boolean
     */
    @Override
    public boolean stopJob(String jobName, String jobGroup) {
        LogContext.push(LogType.TRACING, "Stop Job: " + jobName + " group: " + jobGroup);
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            return scheduler.interrupt(jobKey);
        } catch (SchedulerException e) {
            LogContext.push(
                    LogType.TRACING,
                    "SchedulerException while stopping job. error message :"
                            + e.getMessage()
            );
        }
        return false;
    }

    /**
     * @param jobDetail jobDetail
     * @param triggers  triggers
     * @return boolean
     */
    private boolean schedule(JobDetail jobDetail, Set<Trigger> triggers) {
        try {
            scheduler.scheduleJob(jobDetail, triggers, false);
            LogContext.push(LogType.TRACING, "Job with key jobKey :"
                    + jobDetail.getKey().getName()
                    + " and group :"
                    + jobDetail.getKey().getGroup()
                    + " scheduled successfully for date"
            );
            return true;
        } catch (SchedulerException e) {
            LogContext.push(
                    LogType.TRACING,
                    "SchedulerException while scheduling job with key :"
                            + jobDetail.getKey().getName()
                            + " message :"
                            + e.getMessage()
            );
        }
        return false;
    }

    /**
     * createTriggers
     *
     * @param jobDetail    jobDetail
     * @param timeTriggers timeTriggers
     * @return Set<Trigger>
     * @throws InvalidException InvalidException
     */
    private Set<Trigger> createTriggers(JobDetail jobDetail, Set<TimeTrigger> timeTriggers) throws InvalidException {
        Set<Trigger> triggers = new HashSet<>();
        for (TimeTrigger timeTrigger : timeTriggers) {
            triggers.add(createTrigger(jobDetail, timeTrigger));
        }
        return triggers;
    }

    /**
     * createTrigger
     *
     * @param jobDetail   jobDetail
     * @param timeTrigger timeTrigger
     * @return Trigger
     * @throws InvalidException InvalidException
     */
    private Trigger createTrigger(JobDetail jobDetail, TimeTrigger timeTrigger) throws InvalidException {
        Trigger trigger;
        if (StringUtils.hasText(timeTrigger.getCronExpression())) {
            if (!CronExpression.isValidExpression(timeTrigger.getCronExpression())) {
                throw new InvalidException(ScheduleErrorCode.CRON_EXPRESSION_INVALID);
            }
            trigger = JobUtil.createCronTrigger(
                    timeTrigger.getTriggerName(),
                    jobDetail,
                    Date.from(timeTrigger.getReleaseTime().atZone(ZoneId.systemDefault()).toInstant()),
                    timeTrigger.getCronExpression(),
                    SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW
            );
            LogContext.push(
                    LogType.TRACING,
                    "Creating Cron trigger for key :"
                            + jobDetail.getKey().getName()
                            + " at date :"
                            + trigger.getStartTime()
            );
        } else {
            trigger = JobUtil.createSingleTrigger(
                    timeTrigger.getTriggerName(),
                    jobDetail,
                    Date.from(timeTrigger.getReleaseTime().atZone(ZoneId.systemDefault()).toInstant()),
                    SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW,
                    timeTrigger.getRepeatCount(),
                    timeTrigger.getRepeatInterval()
            );
            LogContext.push(
                    LogType.TRACING,
                    "Creating Cron trigger for key :"
                            + jobDetail.getKey().getName()
                            + " at date :"
                            + trigger.getStartTime()
            );
        }
        return trigger;
    }
}
