package com.thanhvh.scheduler.constant;

import org.quartz.Trigger;

/**
 * TriggerState
 */
public enum TriggerState {
    /**
     * PAUSED
     */
    PAUSED(Trigger.TriggerState.PAUSED),
    /**
     * BLOCKED
     */
    BLOCKED(Trigger.TriggerState.BLOCKED),
    /**
     * COMPLETE
     */
    COMPLETE(Trigger.TriggerState.COMPLETE),
    /**
     * ERROR
     */
    ERROR(Trigger.TriggerState.ERROR),
    /**
     * NONE
     */
    NONE(Trigger.TriggerState.NONE),
    /**
     * SCHEDULED
     */
    SCHEDULED(Trigger.TriggerState.NORMAL),
    /**
     * UNKNOWN
     */
    UNKNOWN(null);

    private final Trigger.TriggerState value;

    /**
     * TriggerState
     *
     * @param triggerState triggerState
     */
    TriggerState(Trigger.TriggerState triggerState) {
        this.value = triggerState;
    }

    /**
     * fromState
     *
     * @param state state
     * @return TriggerState
     */
    public static TriggerState fromState(Trigger.TriggerState state) {
        for (TriggerState trigger : TriggerState.values()) {
            if (state.equals(trigger.getTriggerState())) {
                return trigger;
            }
        }
        return UNKNOWN;
    }

    /**
     * getTriggerState
     *
     * @return Trigger.TriggerState
     */
    public Trigger.TriggerState getTriggerState() {
        return value;
    }
}
