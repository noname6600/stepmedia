package com.thanhvh.scheduler.model;

import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Scheduler
 */
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scheduler implements Serializable {

    /**
     * Job name
     */
    private String jobName;

    /**
     * Job group
     */
    private String jobGroup;

    /**
     * Triggers
     */
    @Builder.Default
    private Set<TimeTrigger> triggers = new HashSet<>();

    /**
     * Description
     */
    private String description;

}
