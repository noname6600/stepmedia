package com.thanhvh.scheduler.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * JobConfig
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "common.scheduler.trigger", ignoreUnknownFields = false)
public class JobConfig {

    /**
     * cronJobs
     */
    @JsonProperty("cron-jobs")
    private Map<String, CronAutoJob> cronJobs;

    /**
     * AutoJob
     */
    @Data
    public static class AutoJob {
        /**
         * jobName
         */
        @JsonProperty("job-name")
        protected String jobName;
        /**
         * groupKey
         */
        @JsonProperty("group-key")
        protected String groupKey;
        /**
         * cronExpression
         */
        @JsonProperty("cron-expression")
        protected String cronExpression;
    }

    /**
     * CronAutoJob
     */
    public static class CronAutoJob extends AutoJob {
    }

}
