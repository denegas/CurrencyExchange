package com.github.denegas.controller.utils;

import com.github.denegas.exception.ValidationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

public final class RequestUtils {
    private RequestUtils() {

    }

    public static BigDecimal convertToBigDecimal(String param, String messageOnException) {
        if (param == null || param.isBlank()) {
            return null;
        }
        try {
            return new BigDecimal(param);
        } catch (NumberFormatException e) {
            throw new ValidationException(messageOnException);
        }
    }

    public static String getBaseCurrencyCodeFromPath(String path) {
        if (isPathEmpty(path)) {
            return "";
        }
        return normalizePath(path).substring(0, 3).toUpperCase();
    }

    public static String getTargetCurrencyCodeFromPath(String path) {
        if (isPathEmpty(path)) {
            return "";
        }
        return normalizePath(path).substring(3).toUpperCase();
    }

    public static Optional<String> getPatchParam(BufferedReader requestReader, String name) throws IOException {

        String body = requestReader.lines().collect(Collectors.joining());
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length <= 1){
                continue;
            }
            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
            if (key.equals(name)) {
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                return Optional.of(value);
            }
                return Optional.of("");
        }
        return Optional.empty();
    }

    public static boolean isPathEmpty(String path) {
        return path == null || path.replace("/","").isBlank();
    }

    public static String normalizePath(String path) {
        if (isPathEmpty(path)) {
            return "";
        }
        return path.trim().replace("/", "");
    }
}
