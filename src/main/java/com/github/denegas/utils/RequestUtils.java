package com.github.denegas.utils;

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

        String body;
        try (BufferedReader reader = requestReader) {
            body = reader.lines().collect(Collectors.joining());
        }

        for (String pair : body.split("&")) {
            String[] kv = pair.split("=", 2);
            if (URLDecoder.decode(kv[0], StandardCharsets.UTF_8).equals(name)) {
                if (kv.length > 1) {
                    return Optional.of(URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
                }
                return Optional.of("");
            }
        }
        return Optional.empty();
    }

    public static boolean isPathEmpty(String path) {
        return path == null || path.replace("/", "").isBlank();
    }

    public static String normalizePath(String path) {
        if (isPathEmpty(path)) {
            return "";
        }
        return path.trim().replace("/", "");
    }
}
