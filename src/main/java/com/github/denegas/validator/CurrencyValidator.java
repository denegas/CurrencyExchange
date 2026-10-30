package com.github.denegas.validator;

import com.github.denegas.entity.Currency;

public interface CurrencyValidator extends Validator<Currency> {
    boolean isValidCurrencyCode(String code);
    String validateCode(String code);
}
