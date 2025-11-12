package com.thanhvh.util;

import java.util.Map;

/**
 * Message formatter
 */
public class MessageFormatter {
    static final String DELIM_START = "{";
    static final String DELIM_STOP = "}";

    private MessageFormatter() {
    }

    /**
     * Format
     *
     * @param msg  message format
     * @param args arguments
     * @return message
     */
    public static String format(String msg, Map<String, String> args) {
        if (args == null || msg == null) {
            return msg;
        }
        for (Map.Entry<String, String> arg : args.entrySet()) {
            msg = msg.replace(getDelim(arg.getKey()), arg.getValue());
        }
        return msg;
    }

    /**
     * Get delim
     *
     * @param key key
     * @return {key}
     */
    protected static String getDelim(String key) {
        return DELIM_START +
                key +
                DELIM_STOP;
    }
}
