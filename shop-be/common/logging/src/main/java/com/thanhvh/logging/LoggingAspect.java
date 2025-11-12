package com.thanhvh.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * Logging AOP
 */
@Aspect
@Slf4j
public class LoggingAspect {

    /**
     * Push request data of method to LogContext
     *
     * @param joinPoint {@link JoinPoint}
     */
    @Before("@within(com.thanhvh.logging.Logging) || @annotation(com.thanhvh.logging.Logging)")
    public void logBefore(JoinPoint joinPoint) {
        LogContext.push(getLogType(joinPoint, true), getMethodName(joinPoint), joinPoint.getArgs());
    }

    /**
     * Push response data to LogContext
     *
     * @param joinPoint {@link JoinPoint}
     * @param obj       response
     */
    @AfterReturning(value = "@within(com.thanhvh.logging.Logging) || @annotation(com.thanhvh.logging.Logging)", returning = "obj")
    public void afterReturning(JoinPoint joinPoint, Object obj) {
        LogContext.push(getLogType(joinPoint, false), getMethodName(joinPoint), obj);
    }

    /**
     * Push error data to LogContext when have exception
     *
     * @param joinPoint {@link JoinPoint}
     * @param ex        exception
     */
    @AfterThrowing(value = "@within(com.thanhvh.logging.Logging)  || @annotation(com.thanhvh.logging.Logging)", throwing = "ex")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception ex) {
        LogContext.push(LogType.TRACING, getMethodName(joinPoint), new ExceptionLog(ex, false));
    }

    /**
     * Get method name from join point
     *
     * @param joinPoint {@link JoinPoint}
     * @return Method name
     */
    protected String getMethodName(JoinPoint joinPoint) {
        String method = "";
        if (joinPoint.getSignature() instanceof MethodSignature methodSignature) {
            method = methodSignature.getDeclaringTypeName().concat(".").concat(methodSignature.getName());
        }
        return method;
    }

    /**
     * Get log type
     *
     * @param joinPoint {@link JoinPoint}
     * @param request   true if log request
     * @return LogType of @Logging
     */
    protected LogType getLogType(JoinPoint joinPoint, boolean request) {
        if (joinPoint.getSignature() instanceof MethodSignature methodSignature) {
            Method method = methodSignature.getMethod();
            Logging logging = AnnotatedElementUtils.findMergedAnnotation(method, Logging.class);
            if (logging == null) {
                logging = AnnotationUtils.findAnnotation(method.getDeclaringClass(), Logging.class);
            }
            if (logging != null) {
                return request ? logging.request() : logging.response();
            }
        }
        return LogType.TRACING;
    }
}
