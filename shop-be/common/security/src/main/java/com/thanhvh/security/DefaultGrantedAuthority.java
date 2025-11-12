package com.thanhvh.security;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

/**
 * DefaultGrantedAuthority
 *
 * @see GrantedAuthority
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DefaultGrantedAuthority implements GrantedAuthority {
    /**
     * Role
     */
    private String role;

    @Override
    public String getAuthority() {
        return this.role;
    }
}
