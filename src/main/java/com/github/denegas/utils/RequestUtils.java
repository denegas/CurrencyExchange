package com.github.denegas.utils;

import com.github.denegas.exception.ValidationException;

import java.math.BigDecimal;

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
}
