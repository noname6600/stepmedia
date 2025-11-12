package com.thanhvh.security;

import com.thanhvh.security.properties.SecurityProperties;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Supplier;

/**
 * Decoder factory
 */
public class DefaultJwtDecoderFactory implements JwtDecoderFactory<SecurityProperties.Jwt> {

    private void jwsAlgorithms(Set<SignatureAlgorithm> signatureAlgorithms, SecurityProperties.Jwt context) {
        for (String algorithm : context.getJwsAlgorithms()) {
            signatureAlgorithms.add(SignatureAlgorithm.from(algorithm));
        }
    }

    private OAuth2TokenValidator<Jwt> getValidators(Supplier<OAuth2TokenValidator<Jwt>> defaultValidator, SecurityProperties.Jwt context) {
        OAuth2TokenValidator<Jwt> defaultValidators = defaultValidator.get();
        List<String> audiences = context.getAudiences();
        if (CollectionUtils.isEmpty(audiences)) {
            return defaultValidators;
        }
        List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();
        validators.add(defaultValidators);
        validators.add(
                new JwtClaimValidator<>(
                        JwtClaimNames.AUD,
                        aud -> aud != null && !Collections.disjoint((Collection<?>) aud, audiences)
                )
        );
        return new DelegatingOAuth2TokenValidator<>(validators);
    }

    @Override
    public JwtDecoder createDecoder(SecurityProperties.Jwt context) {
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withJwkSetUri(context.getJwkSetUri())
                .jwsAlgorithms(
                        signatureAlgorithms -> jwsAlgorithms(signatureAlgorithms, context)
                ).build();
        String issuerUri = context.getIssuerUri();
        Supplier<OAuth2TokenValidator<Jwt>> defaultValidator = (issuerUri != null)
                ? () -> JwtValidators.createDefaultWithIssuer(issuerUri) : JwtValidators::createDefault;
        nimbusJwtDecoder.setJwtValidator(getValidators(defaultValidator, context));
        return nimbusJwtDecoder;
    }

}
