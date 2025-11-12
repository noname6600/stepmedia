package com.thanhvh.security.configuration;

import com.thanhvh.exception.ErrorCode;
import com.thanhvh.exception.RestException;
import com.thanhvh.security.*;
import com.thanhvh.security.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoderFactory;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.*;

/**
 * Security config factory
 *
 * @see #filterChain(HttpSecurity)
 * @see #configureFilter(HttpSecurity)
 * @see #configureAuthorizeRequests(HttpSecurity)
 * @see #cors(HttpSecurity)
 * @see #buildCorsConfiguration()
 */
public class SecurityConfigFactory {
    /**
     * Properties
     */
    protected final SecurityProperties securityProperties;
    /**
     * {@link DefaultJwtDecoderFactory}
     */
    protected JwtDecoderFactory<SecurityProperties.Jwt> jwtDecoderFactory = new DefaultJwtDecoderFactory();
    Logger logger = LoggerFactory.getLogger(SecurityConfigFactory.class);

    /**
     * @param properties {@link SecurityProperties}
     */
    public SecurityConfigFactory(SecurityProperties properties) {
        this.securityProperties = properties;
    }

    /**
     * Config filter chain
     *
     * @param http {@link HttpSecurity}
     * @return {@link SecurityFilterChain}
     */
    public SecurityFilterChain filterChain(HttpSecurity http) {
        try {
            http.cors().and().csrf().disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            this.cors(http);
            this.configureAuthorizeRequests(http);
            this.configureFilter(http);
            return http.build();
        } catch (Exception e) {
            throw RestException.create(ErrorCode.SERVER_ERROR, e);
        }
    }

    /**
     * Configure filter
     *
     * @param http {@link HttpSecurity}
     * @see BearerTokenFilter
     * @see ApiKeyFilter
     */
    protected void configureFilter(HttpSecurity http) {
        if (securityProperties.getOauth2ResourceServer() != null) {
            logger.debug("Oauth2 Resource Server {}", securityProperties.getOauth2ResourceServer());
            for (Map.Entry<String, SecurityProperties.Jwt> jwt : securityProperties.getOauth2ResourceServer().entrySet()) {
                http.addFilterAfter(jwtTokenFilter(jwt.getValue(), jwt.getValue().getPattern()), LogoutFilter.class);
            }
        }
        if (securityProperties.getApiKey() != null) {
            logger.debug("ApiKey {}", securityProperties.getApiKey());
            for (SecurityProperties.ApiKey service : securityProperties.getApiKey()) {
                http.addFilterAfter(new ApiKeyFilter(service.getPath(), service.getKey(), service.getWhiteListIp()), LogoutFilter.class);
            }
        }
    }

    /**
     * Config path matcher
     *
     * @param http {@link HttpSecurity}
     * @throws Exception exception
     */
    protected void configureAuthorizeRequests(HttpSecurity http) throws Exception {
        SecurityProperties.PathMatcherConfig pathMatcherConfig = this.securityProperties.getPathMatcher();
        logger.debug("PathMatcher {}", pathMatcherConfig);
        if (Objects.nonNull(pathMatcherConfig) && pathMatcherConfig.getPermitAllMethods() != null) {
            for (HttpMethod method : pathMatcherConfig.getPermitAllMethods()) {
                http.authorizeHttpRequests().requestMatchers(method).permitAll();
            }
        }

        if (Objects.nonNull(pathMatcherConfig) && pathMatcherConfig.getPermitAllPathPatterns() != null) {
            for (String pattern : pathMatcherConfig.getPermitAllPathPatterns()) {
                http.authorizeHttpRequests().requestMatchers(pattern).permitAll();
            }
        }

        if (Objects.nonNull(pathMatcherConfig) && pathMatcherConfig.getPermitAllMap() != null) {
            for (Map.Entry<HttpMethod, Set<String>> entry : pathMatcherConfig.getPermitAllMap().entrySet()) {
                for (String pattern : entry.getValue()) {
                    http.authorizeHttpRequests().requestMatchers(entry.getKey(), pattern).permitAll();
                }
            }
        }
        http.authorizeHttpRequests().anyRequest().authenticated().and().exceptionHandling()
                .authenticationEntryPoint(
                        new DefaultAuthenticationEntryPoint()
                );
    }

    /**
     * Config cors
     *
     * @param http {@link HttpSecurity}
     * @throws Exception exception
     */
    protected void cors(HttpSecurity http) throws Exception {
        CorsConfiguration configuration = buildCorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        http.cors(cors -> cors.configurationSource(source));
    }

    /**
     * Build cors
     *
     * @return {@link CorsConfiguration}
     */
    protected CorsConfiguration buildCorsConfiguration() {
        SecurityProperties.Cors cors = this.securityProperties.getCors();
        logger.debug("Cors {}", cors);
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        if (Objects.nonNull(cors)) {
            if (Objects.nonNull(cors.getAllowedOrigins())) {
                List<String> origins = new ArrayList<>();
                for (String allowedOrigin : cors.getAllowedOrigins()) {
                    origins.addAll(Arrays.asList(allowedOrigin.split("\\s*,\\s*")));
                }
                configuration.setAllowedOrigins(origins);
            }

            if (Objects.nonNull(cors.getAllowedMethods())) {
                configuration.setAllowedMethods(cors.getAllowedMethods());
            }

            if (Objects.nonNull(cors.getAllowedHeaders())) {
                configuration.setAllowedHeaders(cors.getAllowedHeaders());
            }
        }
        return configuration;
    }

    /**
     * Make BearerToken filter from {@link SecurityProperties#getOauth2ResourceServer()}
     *
     * @param jwt     {@link SecurityProperties.Jwt}
     * @param pattern pattern
     * @return {@link BearerTokenFilter}
     */
    protected BearerTokenFilter jwtTokenFilter(SecurityProperties.Jwt jwt, String pattern) {
        if (pattern == null || pattern.isBlank()) {
            return new BearerTokenFilter(authenticationProvider(jwt));
        }
        return new BearerTokenFilter(authenticationProvider(jwt), pattern);
    }

    /**
     * Make Authentication provider
     *
     * @param jwt {@link SecurityProperties.Jwt}
     * @return {@link AuthenticationProvider}
     */
    protected AuthenticationProvider authenticationProvider(SecurityProperties.Jwt jwt) {
        JwtDecoder decoder = jwtDecoderFactory.createDecoder(
                jwt
        );
        return new JwtAuthProvider(decoder);
    }
}
