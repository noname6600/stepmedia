package com.thanhvh.logging;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Push an exception in LogContainer
 */
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ExceptionLog implements Serializable {
    /**
     * {@link Exception} simple name
     */
    private final String exception;
    /**
     * {@link Exception} message
     */
    private final String message;

    /**
     * Custom message
     */
    private final String errorMessage;
    /**
     * throw at
     */
    private final String throwAt;
    /**
     * line
     */
    private final int line;
    /**
     * stack trace
     */
    private final List<String> stackTrace;

    /**
     * {@link ExceptionLog}
     *
     * @param e             an exception
     * @param logStackTrace Stack trace or not
     */
    public ExceptionLog(Exception e, boolean logStackTrace) {
        this.exception = e.getClass().getSimpleName();
        this.message = e.getMessage();
        this.errorMessage = null;
        this.throwAt = e.getStackTrace()[0].getClassName();
        this.line = e.getStackTrace()[0].getLineNumber();

        if (logStackTrace) {
            stackTrace = Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList();
        } else {
            stackTrace = null;
        }
    }

    /**
     * {@link ExceptionLog}
     *
     * @param e             an exception
     * @param message       specified message
     * @param logStackTrace stack trace or not
     */
    public ExceptionLog(Exception e, String message, boolean logStackTrace) {
        this.exception = e.getClass().getSimpleName();
        this.message = e.getMessage();
        this.errorMessage = message;
        this.throwAt = e.getStackTrace()[0].getClassName();
        this.line = e.getStackTrace()[0].getLineNumber();

        if (logStackTrace) {
            stackTrace = Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList();
        } else {
            stackTrace = null;
        }
    }

    /**
     * Get stack trace
     *
     * @return get stack trace
     */
    public List<String> getStackTrace() {
        if (stackTrace == null) {
            return Collections.emptyList();
        }
        return List.copyOf(stackTrace);
    }

    @Override
    public String toString() {
        return "ExceptionLog{" +
                "exception='" + exception + '\'' +
                ", message='" + message + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", throwAt='" + throwAt + '\'' +
                ", line=" + line +
                ", stackTrace=" + stackTrace +
                '}';
    }
}
