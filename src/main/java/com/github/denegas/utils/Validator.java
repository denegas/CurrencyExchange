package com.github.denegas.utils;

import com.github.denegas.entity.Currency;

public final class Validator {
    private static final int CURRENCY_CODE_LENGTH = 3;

    private Validator() {
    }

    public static boolean isValidCurrency(Currency currency) {
        return currency != null && isValidCurrencyCode(currency.getCode())
                && isValidCurrencyFullName(currency) && isValidCurrencySign(currency);
    }

    public static boolean isValidCurrencyCode(String code) {
        if (code == null) {
            return false;
        }
        return code.length() == CURRENCY_CODE_LENGTH && code.equals(code.toUpperCase());
    }

    private static boolean isValidCurrencyFullName(Currency currency) {
        return currency.getFullName() != null && !currency.getFullName().isBlank();
    }

    private static boolean isValidCurrencySign(Currency currency) {
        return currency.getSign() != null && !currency.getSign().isBlank();
    }
}
