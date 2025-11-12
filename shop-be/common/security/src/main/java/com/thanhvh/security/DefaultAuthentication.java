package com.thanhvh.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;

/**
 * DefaultAuthentication
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class DefaultAuthentication extends AbstractAuthenticationToken implements Authentication {
    /**
     * User detail
     */
    private UserDetails principal;
    /**
     * Token
     */
    private String token;

    /**
     * @param principal {@link UserDetails}
     * @param token     token
     */
    public DefaultAuthentication(UserDetails principal, String token) {
        this(token);
        this.principal = principal;
    }

    /**
     * @param token token
     */
    public DefaultAuthentication(String token) {
        super(Collections.emptyList());
        Assert.hasText(token, "token cannot be empty");
        this.token = token;
    }

    /**
     * @param principal   {@link UserDetails}
     * @param token       token
     * @param authorities {@link GrantedAuthority}
     */
    public DefaultAuthentication(UserDetails principal, String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        Assert.hasText(token, "token cannot be empty");
        this.token = token;
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return this.getToken();
    }


    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }
}
