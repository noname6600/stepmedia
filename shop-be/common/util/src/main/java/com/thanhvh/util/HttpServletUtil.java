package com.thanhvh.util;


import com.thanhvh.util.constant.Language;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * HttpServletUtil
 */
public class HttpServletUtil {

    private static final String HTTP_HEADER_USER_AGENT = "user-agent";
    private static final String HTTP_HEADER_X_FORWARDED_FOR = "X-Forwarded-For";

    private HttpServletUtil() {
    }

    /**
     * @return user agent from header
     */
    public static String getUserAgent() {
        return getUserAgent(getRequest());
    }

    /**
     * Get user agent
     *
     * @param httpServletRequest {@link HttpServletRequest}
     * @return user agent
     */
    public static String getUserAgent(HttpServletRequest httpServletRequest) {
        if (httpServletRequest == null) {
            return null;
        }
        return httpServletRequest.getHeader(HTTP_HEADER_USER_AGENT);
    }

    /**
     * @return client ip address
     */
    public static String getClientIP() {
        return getClientIP(getRequest());
    }

    /**
     * @param httpServletRequest {@link HttpServletRequest}
     * @return client ip address
     */
    public static String getClientIP(HttpServletRequest httpServletRequest) {
        List<String> remoteIp = getRemoteIp(httpServletRequest);
        if (remoteIp.isEmpty()) {
            return null;
        }
        return remoteIp.get(0);
    }

    /**
     * Get Remote ip
     *
     * @return remote ip
     */
    public static List<String> getRemoteIp() {
        return getRemoteIp(getRequest());
    }

    /**
     * Get Remote ip
     *
     * @param httpServletRequest {@link HttpServletRequest}
     * @return remote ip
     */
    public static List<String> getRemoteIp(HttpServletRequest httpServletRequest) {
        if (httpServletRequest == null) {
            return new ArrayList<>();
        }
        String ipAddress = httpServletRequest.getHeader(HTTP_HEADER_X_FORWARDED_FOR);
        if (ipAddress == null) {
            String remoteAddr = httpServletRequest.getRemoteAddr();
            if (remoteAddr == null) {
                return new ArrayList<>();
            }
            return List.of(httpServletRequest.getRemoteAddr());
        }
        return Arrays.asList(ipAddress.trim().split("\\s*,\\s*"));
    }

    /**
     * @param httpServletRequest {@link HttpServletRequest}
     * @return server host
     */
    public static String getHost(HttpServletRequest httpServletRequest) {
        if (httpServletRequest == null) {
            return "";
        }
        ServletServerHttpRequest httpRequest = new ServletServerHttpRequest(httpServletRequest);
        UriComponents uriComponents = UriComponentsBuilder.fromHttpRequest(httpRequest).build();
        String scheme = uriComponents.getScheme();
        String serverName = httpServletRequest.getServerName();
        int serverPort = httpServletRequest.getServerPort();

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://");
        url.append(serverName);

        if (serverPort != 80 && serverPort != 443) {
            url.append(":").append(serverPort);
        }
        url.append("/");
        return url.toString();
    }

    /**
     * @return server host
     */
    public static String getHost() {
        return getHost(getRequest());
    }

    /**
     * @param path file
     * @return file url
     */
    public static String getFileUrl(String path) {
        if (path == null) {
            return null;
        }
        return getHost() + path;
    }

    /**
     * Get Servlet Request
     *
     * @return {@link HttpServletRequest}
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequest) {
            return servletRequest.getRequest();
        }
        return null;
    }

    /**
     * Get Accept-Language from client, default en {@link Language}
     *
     * @return Accept-Language {@link HttpHeaders}
     */
    public static Language getLanguage() {
        HttpServletRequest request = getRequest();
        if (request == null || request.getLocale() == null) {
            return Language.defaultLanguage();
        }
        return Language.fromISO(request.getLocale().getLanguage());
    }

}
