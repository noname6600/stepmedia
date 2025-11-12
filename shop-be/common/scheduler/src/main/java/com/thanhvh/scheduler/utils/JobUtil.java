package com.thanhvh.scheduler.utils;

import com.thanhvh.scheduler.constant.SchedulerConstants;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 * JobUtil
 */
public interface JobUtil {

    /**
     * createJob
     *
     * @param jobClass    jobClass
     * @param isDurable   isDurable
     * @param context     context
     * @param jobName     jobName
     * @param jobGroup    jobGroup
     * @param jobDataMap  jobDataMap
     * @param description description
     * @return JobDetail
     */
    static JobDetail createJob(Class<? extends Job> jobClass,
                               boolean isDurable,
                               ApplicationContext context,
                               String jobName,
                               String jobGroup,
                               JobDataMap jobDataMap,
                               String description) {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(isDurable);
        factoryBean.setDescription(description);
        factoryBean.setApplicationContext(context);
        factoryBean.setName(jobName);
        factoryBean.setGroup(jobGroup);

        // set job data map
        if (jobDataMap == null) {
            jobDataMap = new JobDataMap();
        }
        jobDataMap.put(jobName + jobGroup, jobClass.getName());
        factoryBean.setJobDataMap(jobDataMap);

        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }

    /**
     * createCronTrigger
     *
     * @param jobDetail          jobDetail
     * @param startTime          startTime
     * @param cronExpression     cronExpression
     * @param misFireInstruction misFireInstruction
     * @return Trigger
     */
    static Trigger createCronTrigger(JobDetail jobDetail,
                                     Date startTime,
                                     String cronExpression,
                                     int misFireInstruction) {
        return createCronTrigger(null, jobDetail, startTime, cronExpression, misFireInstruction);
    }

    /**
     * createCronTrigger
     *
     * @param jobDetail          jobDetail
     * @param triggerName        triggerName
     * @param startTime          startTime
     * @param cronExpression     cronExpression
     * @param misFireInstruction misFireInstruction
     * @return Trigger
     */
    static Trigger createCronTrigger(String triggerName,
                                     JobDetail jobDetail,
                                     Date startTime,
                                     String cronExpression,
                                     int misFireInstruction) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setName(StringUtils.hasText(triggerName) ? triggerName : SchedulerConstants.CRON_TRIGGER_PREFIX_NAME + UUID.randomUUID());
        factoryBean.setStartTime(startTime);
        factoryBean.setGroup(jobDetail.getKey().getGroup());
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setMisfireInstruction(misFireInstruction);
        try {
            factoryBean.afterPropertiesSet();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return factoryBean.getObject();
    }

    /**
     * createSingleTrigger
     *
     * @param jobDetail          jobDetail
     * @param startTime          startTime
     * @param misFireInstruction misFireInstruction
     * @param repeatCount        repeatCount
     * @param repeatInterval     repeatInterval
     * @return Trigger
     */
    static Trigger createSingleTrigger(JobDetail jobDetail,
                                       Date startTime,
                                       int misFireInstruction,
                                       int repeatCount,
                                       long repeatInterval) {
        return createSingleTrigger(null, jobDetail, startTime, misFireInstruction, repeatCount, repeatInterval);
    }

    /**
     * createSingleTrigger
     *
     * @param triggerName        triggerName
     * @param jobDetail          jobDetail
     * @param startTime          startTime
     * @param misFireInstruction misFireInstruction
     * @param repeatCount        repeatCount
     * @param repeatInterval     repeatInterval
     * @return Trigger
     */
    static Trigger createSingleTrigger(String triggerName,
                                       JobDetail jobDetail,
                                       Date startTime,
                                       int misFireInstruction,
                                       int repeatCount,
                                       long repeatInterval) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setName(StringUtils.hasText(triggerName) ? triggerName : SchedulerConstants.PREFIX_SINGLE_TRIGGER_NAME + UUID.randomUUID());
        factoryBean.setGroup(jobDetail.getKey().getGroup());
        factoryBean.setStartTime(startTime);
        factoryBean.setMisfireInstruction(misFireInstruction);
        factoryBean.setRepeatCount(repeatCount);
        factoryBean.setRepeatInterval(repeatInterval);
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }
}
