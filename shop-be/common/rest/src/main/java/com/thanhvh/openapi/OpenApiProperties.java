package com.thanhvh.openapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Open api configuration multiple server
 */
@ConfigurationProperties("common.rest.open-api")
public class OpenApiProperties {
    /**
     * Servers
     */
    @JsonProperty("servers")
    private List<String> servers;

    /**
     * Get servers
     *
     * @return list server
     */
    public List<String> getServers() {
        return servers;
    }

    /**
     * Set server
     *
     * @param servers server
     */
    public void setServers(List<String> servers) {
        this.servers = servers;
    }
}
