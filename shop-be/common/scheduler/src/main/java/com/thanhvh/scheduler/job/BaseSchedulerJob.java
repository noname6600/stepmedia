package com.thanhvh.scheduler.job;

import com.thanhvh.exception.InvalidException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * BaseSchedulerJob
 */
@Slf4j
public abstract class BaseSchedulerJob extends QuartzJobBean {

    /**
     * COUNT_KEY
     */
    public static final String COUNT_KEY = "count";

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        int count = 0;

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        if (dataMap.containsKey(COUNT_KEY)) {
            count = dataMap.getIntValue(COUNT_KEY);
        }

        if (count >= 3) {
            log.error("Job {} fail many time", context.getJobDetail().getKey().getName());
            JobExecutionException e = new JobExecutionException("Retries exceeded");
            e.setUnscheduleAllTriggers(true);
            throw e;
        }

        try {
            job(context.getJobDetail());
            dataMap.putAsString(COUNT_KEY, 0);
        } catch (Exception e) {
            log.error("Job: {} throw exception {}", context.getJobDetail().getKey().getName(), e.getMessage());
            count++;
            dataMap.putAsString(COUNT_KEY, count);
            JobExecutionException e2 = new JobExecutionException(e);
            e2.refireImmediately();
            throw e2;
        }
    }

    /**
     * job
     *
     * @param jobDetail JobDetail
     * @throws InvalidException InvalidException
     */
    protected abstract void job(JobDetail jobDetail) throws InvalidException;

}
