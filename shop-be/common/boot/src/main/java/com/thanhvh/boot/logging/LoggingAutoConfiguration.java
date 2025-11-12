package com.thanhvh.boot.logging;

import com.thanhvh.interceptor.InterceptorConfigurer;
import com.thanhvh.logging.LoggingAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Logging config
 */
@AutoConfiguration
public class LoggingAutoConfiguration {
    private LoggingAutoConfiguration() {
    }

    /**
     * Logging aspect
     *
     * @return {@link LoggingAspect}
     */
    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }

    /**
     * Logging interceptor
     *
     * @return {@link WebMvcConfigurer}
     */
    @Bean
    @ConditionalOnMissingBean
    WebMvcConfigurer webMvcConfigurer() {
        return new InterceptorConfigurer();
    }
}
