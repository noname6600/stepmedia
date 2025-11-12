package com.thanhvh.logging;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Log request, response when call API by restClient
 *
 * @param body     body
 * @param url      url
 * @param method   method
 * @param response response
 * @param params   params
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record RestClientLog(
        /**
         * url
         */
        String url,
        /**
         * method
         */
        Object method,
        /**
         * params
         */
        Object params,
        /**
         * body
         */
        Object body,
        /**
         * response
         */
        Object response
) implements Serializable {
}
