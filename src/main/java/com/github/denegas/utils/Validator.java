package com.github.denegas.utils;

import com.github.denegas.entity.Currency;

public final class Validator {
    private static final int CURRENCY_CODE_LENGTH = 3;
    private Validator() {
    }

    public static boolean isValidCurrency(Currency currency) {
        return currency != null && isValidCurrencyCode(currency.getCode())
                && !currency.getFullName().isBlank() && !currency.getSign().isBlank();
    }

    public static boolean isValidCurrencyCode(String code) {
        return code.length() == CURRENCY_CODE_LENGTH && code.equals(code.toUpperCase());
    }
}
