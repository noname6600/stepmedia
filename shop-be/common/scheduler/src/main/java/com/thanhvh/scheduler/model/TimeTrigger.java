package com.thanhvh.scheduler.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * TimeTrigger
 */
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class TimeTrigger implements Serializable {

    /**
     * Trigger name
     */
    private String triggerName;

    /**
     * CronExpression.
     * If cronExpression have value,
     * after release notification will schedule to send follow cronExpression
     */
    private String cronExpression;

    /**
     * Release time
     */
    @NonNull
    private LocalDateTime releaseTime;

    /**
     * For Single trigger
     */
    @Builder.Default
    private int repeatCount = 0;

    /**
     * For Single trigger. Milliseconds
     */
    @Builder.Default
    private long repeatInterval = 0;
}
