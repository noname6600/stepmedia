package com.thanhvh.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.google.protobuf.Message;
import com.googlecode.protobuf.format.FormatFactory;
import com.googlecode.protobuf.format.ProtobufFormatter;
import com.thanhvh.util.constant.Constants;
import jakarta.servlet.Filter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpEntity;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Log context
 */
public final class LogContext {

    private static final Logger logger = LoggerFactory.getLogger(LogContext.class);
    private static final Map<String, LogContainer> loggingMap = new ConcurrentHashMap<>();
    private static final ProtobufFormatter jsonFormat = new FormatFactory().createFormatter(FormatFactory.Formatter.JSON);
    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(DeserializationFeature.UNWRAP_ROOT_VALUE)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setAnnotationIntrospector(
                    new JacksonAnnotationIntrospector() {
                        @Override
                        public boolean hasIgnoreMarker(AnnotatedMember m) {
                            return _findAnnotation(m, LogIgnore.class) != null;
                        }

                        @Override
                        public PropertyName findNameForSerialization(Annotated a) {
                            if (a.hasAnnotation(LogIgnore.class)) {
                                return null;
                            }
                            return super.findNameForSerialization(a);
                        }
                    }
            ).findAndRegisterModules();
    private static final List<Class<?>> logIgnoreClass = new ArrayList<>();
    private static final Map<Class<?>, Function<?, ?>> customLog = new HashMap<>();

    private LogContext() {
    }

    /**
     * Add log
     *
     * @param logType log type
     * @param data    log data
     */
    public static void push(LogType logType, Object data) {
        push(logType, null, data);
    }

    /**
     * Add log
     *
     * @param logType log type
     * @param method  method name
     * @param data    log data
     */
    public static void push(LogType logType, String method, Object data) {
        if (logType.equals(LogType.IGNORE)) {
            return;
        }
        try {
            String sessionId = MDC.get(Constants.SESSION_ID);
            String userId = MDC.get(Constants.USERID);
            String fullName = MDC.get(Constants.FULL_NAME);
            String path = MDC.get(Constants.PATH);
            Object serializableData = serializableObject(data);
            logger.debug("SessionId: {} | UserId: {} | FullName: {} | Path: {} | Data: {}}", sessionId, userId, fullName, path, serializableData);
            if (sessionId != null) {
                loggingMap.compute(
                        sessionId,
                        (s, logContainer) -> {
                            if (logContainer == null) {
                                logContainer = new LogContainer(s, userId, fullName, path);
                            }
                            logContainer.addLog(new LogObject(logType, serializableData, method, logContainer));
                            return logContainer;
                        }
                );
            }
        } catch (Exception e) {
            logger.warn("LogContext.push data {} error in {}: {}", data, e.getClass().getSimpleName(), e.getMessage());
        }
    }

    /**
     * Get LogContainer of session
     *
     * @param sessionId session
     * @return log value
     * @throws JsonProcessingException json processing exception
     */
    public static String pop(String sessionId) throws JsonProcessingException {
        if (loggingMap.containsKey(sessionId)) {
            LogContainer logContainer = loggingMap.get(sessionId);
            loggingMap.remove(sessionId);
            String raw = mapper.writeValueAsString(logContainer);
            return raw.replace("\"{\\", "{")
                    .replace("}\"", "}")
                    .replace("\\\"", "\"");
        }
        return null;
    }

    private static Object serializableObject(Object obj) {
        if (obj == null) {
            return null;
        }
        Function function = customLog.get(obj.getClass());
        if (function != null) {
            return function.apply(obj);
        }

        if (obj instanceof Message message) {
            return jsonFormat.printToString(message);
        }

        if (obj instanceof Collection<?> collection) {
            List<Object> result = new ArrayList<>();
            for (Object object : collection) {
                result.add(serializableObject(object));
            }
            return result;
        }

        if (obj instanceof Object[] objects) {
            List<Object> result = new ArrayList<>();
            for (Object object : objects) {
                result.add(serializableObject(object));
            }
            return result;
        }

        if (obj instanceof HttpEntity<?> entity) {
            return serializableObject(entity.getBody());
        }


        if (obj instanceof RestClientLog restClientLog) {
            List<Object> result = new ArrayList<>();
            result.add(serializableObject(restClientLog.url()));
            result.add(serializableObject(restClientLog.method()));
            result.add(serializableObject(restClientLog.params()));
            result.add(serializableObject(restClientLog.body()));
            result.add(serializableObject(restClientLog.response()));
            return result;
        }

        if (obj instanceof InputStreamSource) {
            return null;
        }

        if (ignoreType(obj)) {
            return null;
        }
        return obj;
    }

    private static boolean ignoreType(Object obj) {
        if (obj instanceof ServletRequest) {
            return true;
        }
        if (obj instanceof ServletResponse) {
            return true;
        }
        if (logIgnoreClass.contains(obj.getClass())) {
            return true;
        }
        return obj instanceof Filter;
    }

    /**
     * Add class not log
     *
     * @param ignoreClass class
     */
    public static void addLogIgnoreClass(Class<?>... ignoreClass) {
        logIgnoreClass.addAll(List.of(ignoreClass));
    }

    /**
     * Custom log
     *
     * @param <R>      R
     * @param <T>      T
     * @param clazz    class
     * @param function function
     */
    public static <T, R> void addCustomLog(Class<T> clazz, Function<T, R> function) {
        customLog.put(clazz, function);
    }
}
