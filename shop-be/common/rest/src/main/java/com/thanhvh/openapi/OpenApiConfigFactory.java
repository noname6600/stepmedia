package com.thanhvh.openapi;

import com.thanhvh.rest.factory.response.DefaultResponseFactory;
import com.thanhvh.rest.factory.response.IResponseFactory;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.GlobalOperationCustomizer;

import java.util.Comparator;
import java.util.Objects;

/**
 * OpenApiConfigFactory
 */
public record OpenApiConfigFactory(OpenApiProperties openApiProperties) {
    /**
     * OpenAPI
     *
     * @return {@link OpenAPI}
     */
    public OpenAPI openAPI() {
        if (openApiProperties.getServers() == null) {
            return new OpenAPI();
        }
        return new OpenAPI().servers(
                openApiProperties.getServers().stream().map(serverUrl -> new Server().url(serverUrl)).toList()
        );
    }

    /**
     * OperationIdCustomizer
     *
     * @return {@link GlobalOperationCustomizer}
     */
    public GlobalOperationCustomizer operationIdCustomizer() {
        return (operation, handlerMethod) -> {
            Class<?> superClazz = handlerMethod.getBeanType().getSuperclass();
            if (Objects.nonNull(superClazz)) {
                String beanName = handlerMethod.getBeanType().getSimpleName();
                operation.setOperationId(String.format("%s_%s", beanName, handlerMethod.getMethod().getName()));
            }
            return operation;
        };
    }

    /**
     * SortTagsAlphabetically
     *
     * @return {@link GlobalOpenApiCustomizer}
     */
    public GlobalOpenApiCustomizer sortTagsAlphabetically() {
        return openApi -> {
            if (openApi != null && openApi.getTags() != null) {
                openApi.setTags(
                        openApi.getTags()
                                .stream()
                                .sorted(Comparator.comparing(tag -> StringUtils.stripAccents(tag.getName())))
                                .toList()
                );
            }
        };
    }

    /**
     * IResponseFactory
     *
     * @return {@link IResponseFactory}
     */
    public IResponseFactory responseFactory() {
        return new DefaultResponseFactory();
    }

}
