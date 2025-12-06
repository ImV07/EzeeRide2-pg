package com.project.util;

public class TextNormalizer {
    public static String upper(String value) {
        return (value == null) ? null : value.trim().toUpperCase();
    }
}
