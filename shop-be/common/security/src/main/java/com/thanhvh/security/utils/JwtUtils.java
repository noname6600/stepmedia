package com.thanhvh.security.utils;

import com.thanhvh.security.DefaultUserDetail;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.UUID;

/**
 * Jwt Utils
 */
public interface JwtUtils {

    /**
     * Get userId UUID
     *
     * @return userId
     */
    static UUID getUserId() {
        return UUID.fromString(getUserIdString());
    }

    /**
     * Get claim from token by key
     *
     * @param key key
     * @return Object claim
     */
    static Object getClaim(String key) {
        DefaultUserDetail detail = getUserDetailFromToken();
        Map<String, Object> claims = detail.getClaims();
        return claims.get(key);
    }

    /**
     * Get userId String
     *
     * @return userId
     */
    static String getUserIdString() {
        return getUserDetailFromToken().getSubject();
    }

    /**
     * Get sessionState
     *
     * @return sessionState
     */
    static String getSessionFromToken() {
        return getUserDetailFromToken().getSessionState();
    }

    /**
     * Get user detail from security context
     *
     * @return userDetail;
     */
    static DefaultUserDetail getUserDetailFromToken() {
        return (DefaultUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
