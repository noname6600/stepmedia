package com.thanhvh.security.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Security properties
 */
@ConfigurationProperties("common.security")
public class SecurityProperties {

    @JsonProperty("cors")
    private Cors cors;

    @JsonProperty("path-matcher")
    private PathMatcherConfig pathMatcher;

    @JsonProperty("oauth2-resource-server")
    private Map<String, Jwt> oauth2ResourceServer;

    @JsonProperty("api-key")
    private List<ApiKey> apiKey;

    /**
     * @return {@link #cors}
     */
    public Cors getCors() {
        return cors;
    }

    /**
     * @param cors {@link Cors}
     */
    public void setCors(Cors cors) {
        this.cors = cors;
    }

    /**
     * @return {@link #pathMatcher}
     */
    public PathMatcherConfig getPathMatcher() {
        return pathMatcher;
    }

    /**
     * @param pathMatcher {@link PathMatcherConfig}
     */
    public void setPathMatcher(PathMatcherConfig pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    /**
     * @return {@link #oauth2ResourceServer}
     */
    public Map<String, Jwt> getOauth2ResourceServer() {
        return oauth2ResourceServer;
    }

    /**
     * @param oauth2ResourceServer oauth2ResourceServer
     */
    public void setOauth2ResourceServer(Map<String, Jwt> oauth2ResourceServer) {
        this.oauth2ResourceServer = oauth2ResourceServer;
    }

    /**
     * @return {@link ApiKey}
     */
    public List<ApiKey> getApiKey() {
        return apiKey;
    }

    /**
     * @param apiKey {@link ApiKey}
     */
    public void setApiKey(List<ApiKey> apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * PathMatcher properties
     */
    public static class PathMatcherConfig {
        @JsonProperty("permit-all-methods")
        private Set<HttpMethod> permitAllMethods = null;

        @JsonProperty("permit-all-path-patterns")
        private Set<String> permitAllPathPatterns = null;

        @JsonProperty("permit-all-map")
        private Map<HttpMethod, Set<String>> permitAllMap = null;

        /**
         * @return {@link #permitAllMethods}
         */
        public Set<HttpMethod> getPermitAllMethods() {
            return permitAllMethods;
        }

        /**
         * @param permitAllMethods permitAllMethods
         */
        public void setPermitAllMethods(Set<HttpMethod> permitAllMethods) {
            this.permitAllMethods = permitAllMethods;
        }

        /**
         * @return {@link #permitAllPathPatterns}
         */
        public Set<String> getPermitAllPathPatterns() {
            return permitAllPathPatterns;
        }

        /**
         * @param permitAllPathPatterns permitAllPathPatterns
         */
        public void setPermitAllPathPatterns(Set<String> permitAllPathPatterns) {
            this.permitAllPathPatterns = permitAllPathPatterns;
        }

        /**
         * @return {@link #permitAllMap}
         */
        public Map<HttpMethod, Set<String>> getPermitAllMap() {
            return permitAllMap;
        }

        /**
         * @param permitAllMap permitAllMap
         */
        public void setPermitAllMap(Map<HttpMethod, Set<String>> permitAllMap) {
            this.permitAllMap = permitAllMap;
        }

        @Override
        public String toString() {
            return "PathMatcherConfig{" +
                    "permitAllMethods=" + permitAllMethods +
                    ", permitAllPathPatterns=" + permitAllPathPatterns +
                    ", permitAllMap=" + permitAllMap +
                    '}';
        }
    }

    /**
     * Cors properties
     */
    public static class Cors {
        @JsonProperty("allowed-origins")
        private List<String> allowedOrigins;

        @JsonProperty("allowed-methods")
        private List<String> allowedMethods;

        @JsonProperty("allowed-headers")
        private List<String> allowedHeaders;

        /**
         * @return {@link #allowedOrigins}
         */
        public List<String> getAllowedOrigins() {
            return allowedOrigins;
        }

        /**
         * @param allowedOrigins allowedOrigins
         */
        public void setAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        /**
         * @return {@link #allowedMethods}
         */
        public List<String> getAllowedMethods() {
            return allowedMethods;
        }

        /**
         * @param allowedMethods allowedMethods
         */
        public void setAllowedMethods(List<String> allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        /**
         * @return {@link #allowedHeaders}
         */
        public List<String> getAllowedHeaders() {
            return allowedHeaders;
        }

        /**
         * @param allowedHeaders setAllowedHeaders
         */
        public void setAllowedHeaders(List<String> allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }

        @Override
        public String toString() {
            return "Cors{" +
                    "allowedOrigins=" + allowedOrigins +
                    ", allowedMethods=" + allowedMethods +
                    ", allowedHeaders=" + allowedHeaders +
                    '}';
        }
    }

    /**
     * Jwt properties
     */
    public static class Jwt {
        private String pattern;
        private String jwkSetUri;
        private String issuerUri;
        private List<String> jwsAlgorithms = List.of("RS256");
        private Resource publicKeyLocation;
        private List<String> audiences = new ArrayList<>();

        /**
         * @return {@link #jwkSetUri}
         */
        public String getJwkSetUri() {
            return jwkSetUri;
        }

        /**
         * @param jwkSetUri jwkSetUri
         */
        public void setJwkSetUri(String jwkSetUri) {
            this.jwkSetUri = jwkSetUri;
        }

        /**
         * @return {@link #issuerUri}
         */
        public String getIssuerUri() {
            return issuerUri;
        }

        /**
         * @param issuerUri issuerUri
         */
        public void setIssuerUri(String issuerUri) {
            this.issuerUri = issuerUri;
        }

        /**
         * @return {@link #jwsAlgorithms}
         */
        public List<String> getJwsAlgorithms() {
            return jwsAlgorithms;
        }

        /**
         * @param jwsAlgorithms jwsAlgorithms
         */
        public void setJwsAlgorithms(List<String> jwsAlgorithms) {
            this.jwsAlgorithms = jwsAlgorithms;
        }

        /**
         * @return {@link #publicKeyLocation}
         */
        public Resource getPublicKeyLocation() {
            return publicKeyLocation;
        }

        /**
         * @param publicKeyLocation publicKeyLocation
         */
        public void setPublicKeyLocation(Resource publicKeyLocation) {
            this.publicKeyLocation = publicKeyLocation;
        }

        /**
         * @return {@link #audiences}
         */
        public List<String> getAudiences() {
            return audiences;
        }

        /**
         * @param audiences audiences
         */
        public void setAudiences(List<String> audiences) {
            this.audiences = audiences;
        }

        /**
         * @return {@link #pattern}
         */
        public String getPattern() {
            return pattern;
        }

        /**
         * @param pattern pattern
         */
        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public String toString() {
            return "Jwt{" +
                    "pattern='" + pattern + '\'' +
                    ", jwkSetUri='" + jwkSetUri + '\'' +
                    ", issuerUri='" + issuerUri + '\'' +
                    ", jwsAlgorithms=" + jwsAlgorithms +
                    ", publicKeyLocation=" + publicKeyLocation +
                    ", audiences=" + audiences +
                    '}';
        }
    }

    /**
     * Api key
     */
    public static class ApiKey {
        @JsonProperty("path")
        private String path;

        @JsonProperty("key")
        private String key;

        @JsonProperty("white-list-ip")
        private List<String> whiteListIp;

        /**
         * @return {@link #key}
         */
        public String getKey() {
            return key;
        }

        /**
         * @param key prefixes
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * @return {@link #path}
         */
        public String getPath() {
            return path;
        }

        /**
         * @param path prefix
         */
        public void setPath(String path) {
            this.path = path;
        }

        /**
         * White list ip
         *
         * @return white list ip
         */
        public List<String> getWhiteListIp() {
            return whiteListIp;
        }

        /**
         * Set white list ip
         *
         * @param whiteListIp white list ip
         */
        public void setWhiteListIp(List<String> whiteListIp) {
            this.whiteListIp = whiteListIp;
        }

        @Override
        public String toString() {
            return "ApiKey{" +
                    "path='" + path + '\'' +
                    ", key='" + key + '\'' +
                    ", whiteListIp=" + whiteListIp +
                    '}';
        }
    }
}
