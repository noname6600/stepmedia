package com.thanhvh.logging;

import java.lang.annotation.*;

/**
 * Annotation support log args and return of method
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Logging {
    /**
     * @return Request log type
     */
    LogType request() default LogType.REQUEST;

    /**
     * @return Response log type
     */
    LogType response() default LogType.RESPONSE;
}
