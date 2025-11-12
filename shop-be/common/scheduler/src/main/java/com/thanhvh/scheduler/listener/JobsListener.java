package com.thanhvh.scheduler.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * JobsListener
 */
@Slf4j
public class JobsListener implements JobListener {
    @Override
    public String getName() {
        return "globalJob";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        log.info("Job {} is to be executed", jobExecutionContext.getTrigger().getJobKey().getName());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        log.info("Job {} is vetoed", jobExecutionContext.getTrigger().getJobKey().getName());
    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        if (e != null) {
            log.error("Job {} is executed throw exception {}", jobExecutionContext.getTrigger().getJobKey().getName(), e
                    .getMessage());
        } else {
            log.info("Job {} is executed success", jobExecutionContext.getTrigger().getJobKey().getName());
        }
    }
}
