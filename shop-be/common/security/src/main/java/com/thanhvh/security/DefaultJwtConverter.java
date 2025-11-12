package com.thanhvh.security;

import com.thanhvh.util.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Convert {@link Jwt} to {@link DefaultAuthentication}
 */
@Slf4j
public class DefaultJwtConverter implements Converter<Jwt, DefaultAuthentication> {
    @Override
    public DefaultAuthentication convert(Jwt source) {
        DefaultUserDetail userDetail = MapperUtil.convertValue(source.getClaims(), DefaultUserDetail.class);
        return new DefaultAuthentication(
                userDetail
                        .toBuilder()
                        .claims(source.getClaims())
                        .build()
                , source.getTokenValue(),
                userDetail.getAuthorities()
        );
    }
}
