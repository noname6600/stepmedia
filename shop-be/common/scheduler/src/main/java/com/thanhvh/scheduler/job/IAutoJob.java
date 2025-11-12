package com.thanhvh.scheduler.job;


import com.thanhvh.scheduler.config.JobConfig;

/**
 * IAutoJob
 */
public interface IAutoJob {
    /**
     * getJobConfig
     *
     * @return JobConfig.ShuttleBusJob
     */
    JobConfig.AutoJob getJobConfig();

}
