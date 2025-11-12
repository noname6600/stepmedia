package com.thanhvh.boot.security;

import com.thanhvh.audit.DefaultAuditorAware;
import com.thanhvh.security.configuration.SecurityConfigFactory;
import com.thanhvh.security.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

/**
 * Security config
 */
@AutoConfiguration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityAutoConfiguration {

    private SecurityAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean
    SecurityConfigFactory securityConfig(SecurityProperties properties) {
        return new SecurityConfigFactory(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    SecurityFilterChain filterChain(HttpSecurity http, SecurityConfigFactory config) {
        return config.filterChain(http);
    }

    @Bean
    @ConditionalOnMissingBean
    AuditorAware<UUID> defaultAuditorAware() {
        return new DefaultAuditorAware();
    }
}
