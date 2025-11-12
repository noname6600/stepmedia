package com.thanhvh.security;

import com.thanhvh.util.HttpServletUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Api key filter
 */
@Slf4j
public class ApiKeyFilter extends OncePerRequestFilter implements Filter {
    private static final String API_KEY = "x-api-key";

    private final RequestMatcher matcher;
    private final String pattern;
    private final String key;
    private final List<String> whiteListIps;

    /**
     * API key filter
     *
     * @param pattern pattern
     * @param key     key
     */
    public ApiKeyFilter(String pattern, String key) {
        this(pattern, key, null);
    }

    /**
     * API key filter
     *
     * @param pattern      pattern
     * @param key          key
     * @param whiteListIps white list ip
     */
    public ApiKeyFilter(String pattern, String key, List<String> whiteListIps) {
        this.pattern = pattern;
        this.key = key;
        this.whiteListIps = whiteListIps;
        this.matcher = new AntPathRequestMatcher(pattern);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String requestKey = request.getHeader(API_KEY);
        List<String> remoteIp = HttpServletUtil.getRemoteIp(request);
        log.debug("Remote ip {}", remoteIp);
        if (
                requestKey == null ||
                        !requestKey.equals(this.key) ||
                        (
                                whiteListIps != null
                                        && !whiteListIps.isEmpty()
                                        && checkIp(whiteListIps, remoteIp)
                        )
        ) {
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authenticationResult = new ApiKeyAuthentication(pattern, key, remoteIp);
        context.setAuthentication(authenticationResult);
        SecurityContextHolder.setContext(context);
        filterChain.doFilter(request, response);
    }

    private Boolean checkIp(List<String> whiteList, List<String> ips) {
        for (String s : whiteList) {
            if (ips.contains(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check request match {@link #pattern}
     *
     * @param request {@link HttpServletRequest}
     * @return true if not match pattern
     */
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return !getMatcher().matches(request);
    }

    /**
     * Get {@link #matcher}
     *
     * @return {@link RequestMatcher}
     */
    protected RequestMatcher getMatcher() {
        return matcher;
    }
}
