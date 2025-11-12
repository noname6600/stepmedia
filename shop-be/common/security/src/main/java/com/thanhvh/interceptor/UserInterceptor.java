package com.thanhvh.interceptor;

import com.thanhvh.security.DefaultUserDetail;
import com.thanhvh.util.constant.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * User interceptor
 */
@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {
        try {
            Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user instanceof DefaultUserDetail userPrincipal) {
                MDC.put(Constants.USERID, userPrincipal.getSubject());
                MDC.put(Constants.FULL_NAME, userPrincipal.getName());
            }
        } catch (Exception e) {
            log.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
        }
        return true;
    }
}
