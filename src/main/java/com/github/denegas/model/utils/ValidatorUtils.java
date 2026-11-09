package com.github.denegas.model.utils;

public final class ValidatorUtils {
    private ValidatorUtils() {

    }

    public static boolean isEmptyParameter(String param) {
        return param == null || param.isBlank();
    }
}
