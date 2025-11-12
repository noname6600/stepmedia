package com.thanhvh.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Api key authentication
 */
public class ApiKeyAuthentication implements Authentication {
    /**
     * Key
     */
    private final String key;
    /**
     * Pattern
     */
    private final String pattern;
    /**
     * Remote id
     */
    private final List<String> remoteIp;
    /**
     * authenticated
     */
    private boolean isAuthenticated;

    /**
     * Constructor
     *
     * @param pattern  pattern
     * @param key      key
     * @param remoteIp remote ip
     */
    public ApiKeyAuthentication(String pattern, String key, List<String> remoteIp) {
        this.key = key;
        this.pattern = pattern;
        this.remoteIp = remoteIp;
        this.isAuthenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>();
    }

    @Override
    public Object getCredentials() {
        return getKey();
    }

    @Override
    public Object getDetails() {
        return getCredentials();
    }

    @Override
    public Object getPrincipal() {
        return getCredentials();
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return String.join(",", getRemoteIp());
    }

    /**
     * Pattern
     *
     * @return {@link #pattern}
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Key
     *
     * @return {@link #key}
     */
    public String getKey() {
        return key;
    }

    /**
     * Get list remote ip
     *
     * @return {@link #remoteIp}
     */
    public List<String> getRemoteIp() {
        return remoteIp;
    }
}
