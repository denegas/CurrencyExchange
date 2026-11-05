package com.github.denegas.utils;

import com.github.denegas.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;

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

    public static boolean isInvalid(String param) {
        return param == null || param.isBlank();
    }

    public static BigDecimal convertToBigDecimal(String param, String messageOnException) {
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
        return cleanPath(path).substring(0, 3).toUpperCase();
    }

    public static String getTargetCurrencyCodeFromPath(String path) {
        if (isPathEmpty(path)) {
            return "";
        }
        return cleanPath(path).substring(3).toUpperCase();
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
    private static String cleanPath(String path){
        return path.trim().replace("/","");
    }
}
