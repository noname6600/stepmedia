package com.thanhvh.interceptor;


import com.thanhvh.util.constant.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * Init interceptor
 */
public class InitInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(InitInterceptor.class);

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {
        try {
            MDC.clear();
            MDC.put(Constants.PATH, request.getMethod() + ":" + request.getRequestURI());
            MDC.put(Constants.SESSION_ID, request.getSession().getId());
        } catch (Exception e) {
            logger.warn("Cannot get PATH, SESSION_ID from request because {}", e.getMessage());
        }
        return true;
    }
}
