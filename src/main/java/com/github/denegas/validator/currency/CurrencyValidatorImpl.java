package com.github.denegas.validator.currency;

import com.github.denegas.entity.Currency;
import com.github.denegas.exception.InvalidCurrencyException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyValidatorImpl implements CurrencyValidator {
    private static final int CURRENCY_CODE_LENGTH = 3;
    private static final String codeRegex = "^[A-Z]{3}$";
    private static final Pattern codePattern = Pattern.compile(codeRegex);

    @Override
    public boolean isValid(Currency currency) {
        return currency != null && isValidCurrencyCode(currency.getCode())
                && isValidCurrencyFullName(currency) && isValidCurrencySign(currency);
    }
    @Override
    public boolean isValidCurrencyCode(String code) {
        if (code == null) {
            return false;
        }
        Matcher matcher = codePattern.matcher(code);
        return code.length() == CURRENCY_CODE_LENGTH && code.equals(code.toUpperCase()) && matcher.matches();
    }

    @Override
    public String validateCode(String code) {
        if (code == null) {
            throw new InvalidCurrencyException("currency's code is null");
        }
        return code.trim().toUpperCase().replace("/", "");
    }

    private boolean isValidCurrencyFullName(Currency currency) {
        return currency.getFullName() != null && !currency.getFullName().isBlank();
    }

    private boolean isValidCurrencySign(Currency currency) {
        return currency.getSign() != null && !currency.getSign().isBlank();
    }
}
