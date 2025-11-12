package com.thanhvh.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Gson util
 */
public final class GsonUtil {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
            .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
            .create();

    private GsonUtil() {
    }

    /**
     * @param jsonString json string input
     * @param tClass     Destination class type
     * @param <T>        destination class type
     * @return T object
     */
    public static <T> T map(String jsonString, Class<T> tClass) {
        return gson.fromJson(jsonString, tClass);
    }

    /**
     * @param jsonString json string input
     * @param type       Destination type
     * @param <T>        class return
     * @return T object
     */
    public static <T> T map(String jsonString, Type type) {
        return gson.fromJson(jsonString, type);
    }

    /**
     * @param object input
     * @return String Json
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * @param object input
     * @param clazz  destination class
     * @param <T>    destination class type
     * @return T object
     */
    public static <T> T map(Object object, Class<T> clazz) {
        return gson.fromJson(gson.toJson(object), clazz);
    }

    /**
     * @param object input
     * @param type   destination type
     * @param <T>    destination class type
     * @return T object
     */
    public static <T> T map(Object object, Type type) {
        return gson.fromJson(gson.toJson(object), type);
    }

    /**
     * @param objects list object input
     * @param clazz   destination class
     * @param <T>     destination class type
     * @return List object
     */
    public static <T> List<T> mapList(List<Object> objects, Class<T> clazz) {
        return objects.stream()
                .map(item -> map(item, clazz))
                .toList();
    }

    /**
     * LocalDateSerializer
     */
    public static class LocalDateSerializer implements JsonSerializer<LocalDate> {
        @Override
        public JsonElement serialize(LocalDate localDate, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(Objects.requireNonNull(TimeUtil.toStringDate(localDate.atStartOfDay())));
        }
    }

    /**
     * LocalDateTimeSerializer
     */
    public static class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(Objects.requireNonNull(TimeUtil.toStringDate(localDateTime)));
        }
    }

    /**
     * LocalDateDeserializer
     */
    public static class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return TimeUtil.toLocalDateTime(json.getAsString()).toLocalDate();
        }
    }

    /**
     * LocalDateTimeDeserializer
     */
    public static class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return TimeUtil.toLocalDateTime(json.getAsString());
        }
    }
}
