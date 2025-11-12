package com.thanhvh.util.constant;

import com.thanhvh.util.HttpServletUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Language
 */
public enum Language {
    /**
     * EN
     */
    EN("en", 0),
    /**
     * VI
     */
    VI("vi", 1);

    private final String code;
    private final int order;

    Language(
            String code,
            int order
    ) {
        this.code = code;
        this.order = order;
    }

    /**
     * Get default language
     *
     * @return Language.EN
     */
    public static Language defaultLanguage() {
        return EN;
    }

    /**
     * Get supported languages iso code 2
     *
     * @return supported languages
     */
    public static List<String> supportedLanguages() {
        return Arrays.stream(values()).map(e -> e.code).toList();
    }

    /**
     * Get {@link Language} from ISO code, return default if code not match
     *
     * @param code iso code
     * @return Language
     */

    public static Language fromISO(String code) {
        for (Language value : Language.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return defaultLanguage();
    }

    /**
     * Get message by language context
     *
     * @param defaultMessage default message
     * @param messages       messages ordered: en, vi
     * @return message
     */
    public static String getMessage(String defaultMessage, String... messages) {
        Language language = HttpServletUtil.getLanguage();
        if (messages == null || language.getOrder() > messages.length || language.getOrder() == 0) {
            return defaultMessage;
        }
        return messages[language.order - 1];
    }

    /**
     * Get iso code 2 of current language
     *
     * @return iso code 2
     */
    public String getCode() {
        return code;
    }

    private int getOrder() {
        return order;
    }
}
