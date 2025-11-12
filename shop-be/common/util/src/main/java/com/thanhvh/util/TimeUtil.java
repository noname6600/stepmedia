package com.thanhvh.util;

import com.thanhvh.util.constant.TimePattern;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.TimeZone;

/**
 * Time util
 */
public class TimeUtil {
    private TimeUtil() {
    }

    /**
     * Date to string format dd/MM/yyyy HH:mm:ss
     *
     * @param date date
     * @return string format dd/MM/yyyy HH:mm:ss
     */
    public static String toStringDate(LocalDateTime date) {
        try {
            return toStringDate(date, TimePattern.DD_MM_YYYY_HH_MM_SS);
        } catch (DateTimeException exception) {
            return null;
        }
    }

    /**
     * String format with pattern
     *
     * @param date    date
     * @param pattern pattern
     * @return string value
     */
    public static String toStringDate(LocalDateTime date, TimePattern pattern) {
        try {
            return pattern.getFormatter().format(date);
        } catch (DateTimeException exception) {
            return null;
        }
    }

    /**
     * Long date to string dd/MM/yyyy HH:mm:ss
     *
     * @param date date
     * @return string
     */
    public static String toStringDate(Long date) {
        try {
            return toStringDate(date, TimePattern.DD_MM_YYYY_HH_MM_SS);
        } catch (DateTimeException exception) {
            return null;
        }
    }

    /**
     * @param date    date
     * @param pattern pattern
     * @return string
     */
    public static String toStringDate(Long date, TimePattern pattern) {
        try {
            LocalDateTime localDateTime = toLocalDateTime(date);
            return localDateTime != null ? localDateTime.format(pattern.getFormatter()) : null;
        } catch (DateTimeException exception) {
            return null;
        }
    }

    /**
     * @param dateString date string
     * @return TimeStamp
     * @throws DateTimeParseException parse exception
     */
    public static Long toTimeStamp(String dateString) throws DateTimeParseException {
        return toTimeStamp(dateString, TimePattern.DD_MM_YYYY_HH_MM_SS);
    }

    /**
     * @param dateString dateString
     * @param pattern    pattern
     * @return TimeStamp
     */
    public static Long toTimeStamp(String dateString, TimePattern pattern) {
        if (dateString == null) {
            return null;
        }
        return toTimeStamp(LocalDateTime.parse(dateString, pattern.getFormatter()));
    }

    /**
     * @param dateString dateString
     * @param pattern    pattern
     * @return TimeStamp
     */
    public static Long toTimeStamp(String dateString, String pattern) {
        return toTimeStamp(LocalDateTime.parse(dateString, ofPattern(pattern)));
    }

    /**
     * @param time LocalDateTime
     * @return TimeStamp
     */
    public static Long toTimeStamp(LocalDateTime time) {
        if (time == null) {
            return null;
        }
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * @param stringDate stringDate
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(String stringDate) {
        return toLocalDateTime(stringDate, TimePattern.DD_MM_YYYY_HH_MM_SS);
    }

    public static LocalDate toLocalDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.toLocalDate();
    }

    public static LocalDate toLocalDate(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return toLocalDate(toLocalDateTime(timestamp));
    }

    /**
     * @param stringDate stringDate
     * @param pattern    pattern
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(String stringDate, TimePattern pattern) {
        try {
            return LocalDateTime.parse(stringDate, pattern.getFormatter());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * @param stringDate   stringDate
     * @param pattern      pattern
     * @param defaultValue defaultValue
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(String stringDate, TimePattern pattern, LocalDateTime defaultValue) {
        try {
            return toLocalDateTime(stringDate, pattern);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @param timestamp timestamp
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId());
    }

    /**
     * @param date    LocalDateTime
     * @param pattern pattern
     * @return String
     */
    public static String dateToString(LocalDateTime date, String pattern) {
        try {
            return ofPattern(pattern).format(date);
        } catch (DateTimeException exception) {
            return null;
        }
    }

    /**
     * @param date    timestamp
     * @param pattern pattern
     * @return String
     */
    public static String dateToString(Long date, String pattern) {
        try {
            return ofPattern(pattern).format(toLocalDateTime(date));
        } catch (DateTimeException exception) {
            return null;
        }
    }

    private static DateTimeFormatter ofPattern(String pattern) {
        return new DateTimeFormatterBuilder().appendPattern(pattern)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();
    }

}
