package com.thanhvh.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Mapper util
 */
public final class MapperUtil {
    /**
     * Object mapper
     */
    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(DeserializationFeature.UNWRAP_ROOT_VALUE)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .addHandler(
                    new DeserializationProblemHandler() {
                        @Override
                        public Object handleWeirdStringValue(DeserializationContext context, Class<?> targetType, String valueToConvert, String failureMsg) {
                            return null;
                        }

                        @Override
                        public Object handleWeirdNumberValue(DeserializationContext context, Class<?> targetType, Number valueToConvert, String failureMsg) {
                            return null;
                        }
                    }
            )
            .findAndRegisterModules();

    private MapperUtil() {
    }

    /**
     * Get mapper
     *
     * @return mapper
     */
    public static ObjectMapper getMapper() {
        return mapper;
    }

    /**
     * Write value as string
     *
     * @param value object
     * @return string
     * @throws JsonProcessingException exception
     */
    public static String writeValueAsString(Object value) throws JsonProcessingException {
        return mapper.writeValueAsString(value);
    }

    /**
     * convertValue
     *
     * @param fromValue   fromValue
     * @param toValueType toValueType
     * @param <T>         type
     * @return value
     */
    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return mapper.convertValue(fromValue, toValueType);
    }

    /**
     * convertValue
     *
     * @param fromValue    fromValue
     * @param toValueType  toValueType
     * @param defaultValue defaultValue
     * @param <T>          type
     * @return value
     */
    public static <T> T convertValue(Object fromValue, Class<T> toValueType, T defaultValue) {
        if (fromValue == null) {
            return defaultValue;
        }
        return mapper.convertValue(fromValue, toValueType);
    }

    /**
     * @param source      source
     * @param targetClass targetClass
     * @param <S>         source class
     * @param <T>         target class
     * @return list after convert
     */
    public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source.stream().map(e -> mapper.convertValue(e, targetClass)).toList();
    }

    /**
     * @param serializableObject object
     * @return string
     * @throws MapperException mapper exception
     */
    public static String toString(Object serializableObject) throws MapperException {
        try {
            return mapper.writeValueAsString(serializableObject);
        } catch (JsonProcessingException e) {
            throw new MapperException("Json processing exception");
        }
    }

    /**
     * Serialize
     *
     * @param obj Object
     * @return byte[]
     * @throws IOException exception
     */
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        mapper.writeValue(os, obj);
        return os.toByteArray();
    }

    /**
     * Deserialize
     *
     * @param data byte[]
     * @return Object
     * @throws IOException exception
     */
    public static Object deserialize(byte[] data) throws IOException {
        return deserialize(data, Object.class);
    }

    /**
     * Deserialize
     *
     * @param data  byte[]
     * @param clazz Response class
     * @param <T>   Response class
     * @return object
     * @throws IOException exception
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        return mapper.readValue(data, clazz);
    }
}
