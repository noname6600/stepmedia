package com.thanhvh.interceptor;

import com.thanhvh.logging.LogContext;
import com.thanhvh.util.constant.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * Log interceptor
 */
@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler,
            Exception ex
    ) {
        try {
            String sessionId = MDC.get(Constants.SESSION_ID);
            String logData = LogContext.pop(sessionId);
            if (logData != null) {
                log.info("Debugging for session {}\n{}", sessionId, logData);
            }
        } catch (Exception e) {
            log.warn("LogInterceptor.afterCompletion logging error in {}: {}", e.getClass().getSimpleName(), e.getMessage());
        }
        MDC.clear();
    }
}
