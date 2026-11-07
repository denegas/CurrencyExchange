package com.github.denegas.utils;

public final class ValidatorUtils {
    private ValidatorUtils() {

    }

    public static boolean isEmptyParameter(String param) {
        return param == null || param.isBlank();
    }
}
