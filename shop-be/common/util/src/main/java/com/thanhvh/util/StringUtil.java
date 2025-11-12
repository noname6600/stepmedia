package com.thanhvh.util;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * String util
 */
public class StringUtil {

    private static final SecureRandom secureRandom = new SecureRandom();

    private StringUtil() {

    }

    /**
     * Convert utf-8 to normal text
     *
     * @param string string (chữ cái)
     * @return string string (chu cai)
     */
    public static String normalizeString(String string) {
        String nfdNormalizedString = Normalizer.normalize(string, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString)
                .replaceAll("")
                .replaceAll("đ", "d")
                .replaceAll("Đ", "D");
    }

    /**
     * Convert text to Camel Case
     *
     * @param text (chu cai)
     * @return string (chuCai)
     */
    public static String textToCamelCase(String text) {
        text = normalizeString(text);
        String[] words = text.split("[\\W_]+");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                word = word.isEmpty() ? word : word.toLowerCase();
            } else {
                word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
            }
            builder.append(word);
        }
        return builder.toString();
    }

    /**
     * Convert text to Snake Case
     *
     * @param text (chu cai)
     * @return string (chu_cai)
     */
    public static String textToSnakeCase(String text) {
        text = textToCamelCase(text);
        StringBuilder snakeCase = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if ('A' <= c && c <= 'Z') {
                c += 32;
                if (i != 0) {
                    snakeCase.append('_');
                }
            }
            snakeCase.append(c);
        }
        return snakeCase.toString();
    }

    /**
     * Generate 6 Digits Code
     *
     * @return string
     */
    public static String generate6DigitsCode() {
        return generateDigitsCode(6);
    }

    /**
     * Generate  Digits Code
     *
     * @param number
     * @return string
     */
    public static String generateDigitsCode(long number) {
        StringBuilder ab = new StringBuilder();
        long value;
        if (number < 18) {
            for (long i = 1; i <= number; i++) {
                ab.append(9L);
            }
            value = Long.parseLong(ab.toString());
        } else {
            value = Long.MAX_VALUE;
        }
        String format = "%0longd".replaceAll("long", String.valueOf(number));
        return String.format(format, secureRandom.nextLong(value));
    }

    /**
     * UpperCase without space
     *
     * @param message (abc xyz)
     * @return string (ABCXYZ)
     */
    public static String normalizeUpperCaseString(String message) {
        return normalizeString(message).toUpperCase().replaceAll("\\s+", "");
    }

    /**
     * UpperCase with space
     *
     * @param message (abc xyz)
     * @return string (ABC XYZ)
     */
    public static String normalizeSpaceUpperCaseString(String message) {
        return normalizeString(message).toUpperCase().replaceAll(" +", " ");
    }

    /**
     * UpperCase snakeCase with utf-8
     *
     * @param message (chữ cái)
     * @return string (CHỮ_CÁI)
     */
    public static String normalizeUpperSnakeCase(String message) {
        return message == null ? null : message.toUpperCase().replaceAll("\\s", "_");
    }
}
