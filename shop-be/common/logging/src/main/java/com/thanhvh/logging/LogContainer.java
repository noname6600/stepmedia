package com.thanhvh.logging;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Push all log in session to LogContainer
 */
@Getter
@EqualsAndHashCode
public class LogContainer implements Serializable {
    /**
     * User id
     */
    private final String userId;
    /**
     * Full name
     */
    private final String fullName;
    /**
     * Session id
     */
    private final String sessionId;
    /**
     * Path
     */
    private final String path;
    /**
     * List log object
     */
    private final List<LogObject> logs = new ArrayList<>();
    /**
     * Step
     */
    private int step;
    /**
     * Local date time
     */
    private LocalDateTime stepLastTime;

    /**
     * @param sessionId session
     * @param userId    id of user
     * @param fullName  name of user
     * @param path      path of request or method grpc
     */
    public LogContainer(String sessionId, String userId, String fullName, String path) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.fullName = fullName;
        this.path = path;
        this.step = 0;
        this.stepLastTime = LocalDateTime.now();
    }

    /**
     * @return log step
     */
    public List<LogObject> getLogs() {
        return List.copyOf(logs);
    }

    /**
     * @return last step time
     */
    public LocalDateTime getStepLastTime() {
        return stepLastTime;
    }

    /**
     * @param object step log
     */
    public void addLog(LogObject object) {
        this.logs.add(object);
        step++;
        this.stepLastTime = object.getTime();
    }

    /**
     * @return current step
     */
    public int getStep() {
        return step;
    }

    /**
     * @param objects list steps
     */
    public void addLogs(List<LogObject> objects) {
        this.logs.addAll(objects);
    }

    @Override
    public String toString() {
        return "LogContainer{" +
                "userId='" + userId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", path='" + path + '\'' +
                ", logs=" + logs +
                ", step=" + step +
                ", stepLastTime=" + stepLastTime +
                '}';
    }
}
