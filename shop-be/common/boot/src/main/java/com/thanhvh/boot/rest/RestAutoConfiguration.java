package com.thanhvh.boot.rest;

import com.thanhvh.openapi.OpenApiConfigFactory;
import com.thanhvh.openapi.OpenApiProperties;
import com.thanhvh.rest.exception.ControllerInitBinderAdvice;
import com.thanhvh.rest.factory.response.IResponseFactory;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Rest config
 */
@AutoConfiguration
@EnableConfigurationProperties(OpenApiProperties.class)
@ComponentScan(basePackageClasses = ControllerInitBinderAdvice.class)
@SecuritySchemes(
        value = {
                @SecurityScheme(
                        name = "Authorization",
                        type = SecuritySchemeType.HTTP,
                        bearerFormat = "Bearer [token]",
                        scheme = "bearer",
                        in = SecuritySchemeIn.HEADER,
                        description = "Access token"
                ),
                @SecurityScheme(
                        name = "x-api-key",
                        type = SecuritySchemeType.APIKEY,
                        in = SecuritySchemeIn.HEADER
                )
        }
)
public class RestAutoConfiguration {

    private RestAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean
    OpenApiConfigFactory configFactory(OpenApiProperties openApiProperties) {
        return new OpenApiConfigFactory(openApiProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    IResponseFactory responseFactory(OpenApiConfigFactory factory) {
        return factory.responseFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    OpenAPI openAPI(OpenApiConfigFactory factory) {
        return factory.openAPI();
    }

    @Bean
    @ConditionalOnMissingBean
    GlobalOperationCustomizer operationIdCustomizer(OpenApiConfigFactory factory) {
        return factory.operationIdCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean
    GlobalOpenApiCustomizer sortTagsAlphabetically(OpenApiConfigFactory factory) {
        return factory.sortTagsAlphabetically();
    }
}
