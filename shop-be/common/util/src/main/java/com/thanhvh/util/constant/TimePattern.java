package com.thanhvh.util.constant;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * Time pattern
 */
public enum TimePattern {
    /**
     * dd/MM/yyyy
     */
    DD_MM_YYYY("dd/MM/yyyy"),
    /**
     * dd-MM-yyyy
     */
    DASH_DD_MM_YYYY("dd-MM-yyyy"),
    /**
     * dd/MM/yyyy HH:mm:ss
     */
    DD_MM_YYYY_HH_MM_SS("dd/MM/yyyy HH:mm:ss"),
    /**
     * dd-MM-yyyy HH:mm:ss
     */
    DASH_DD_MM_YYYY_HH_MM_SS("dd-MM-yyyy HH:mm:ss"),
    /**
     * yyyy-MM-dd
     */
    YYYY_MM_DD("yyyy-MM-dd"),
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
    /**
     * HH:mm:ss
     */
    HH_MM_SS("HH:mm:ss");
    private final DateTimeFormatter formatter;
    private final String value;

    TimePattern(String value) {
        this.value = value;
        this.formatter = new DateTimeFormatterBuilder().appendPattern(value)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();
    }

    /**
     * Get value
     *
     * @return {@link #value}
     */
    public String getValue() {
        return value;
    }

    /**
     * Get formatter
     *
     * @return {@link #formatter}
     */
    public DateTimeFormatter getFormatter() {
        return formatter;
    }
}
