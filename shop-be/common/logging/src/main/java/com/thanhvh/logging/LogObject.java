package com.thanhvh.logging;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Log step
 */
@Getter
@EqualsAndHashCode
public class LogObject implements Serializable {

    /**
     * Log type
     */
    private final LogType logType;
    /**
     * Log data
     */
    private final transient Object data;
    /**
     * Time push
     */
    private final LocalDateTime time;
    /**
     * Method name
     */
    private final String method;
    /**
     * step
     */
    private final int step;
    /**
     * Time Millisecond  between this step and before step
     */
    private final long stepTime;

    /**
     * @param logType      Log type
     * @param data         Log data
     * @param method       Method name
     * @param logContainer LogContainer
     */
    public LogObject(LogType logType, Object data, String method, LogContainer logContainer) {
        this.logType = logType;
        this.data = data;
        this.time = LocalDateTime.now();
        this.method = method;
        this.step = logContainer.getStep();
        this.stepTime = Duration.between(logContainer.getStepLastTime(), this.time).toMillis();
    }

    @Override
    public String toString() {
        return "LogObject{" +
                "logType=" + logType +
                ", data=" + data +
                ", time=" + time +
                ", method='" + method + '\'' +
                ", step=" + step +
                ", stepTime=" + stepTime +
                '}';
    }
}
