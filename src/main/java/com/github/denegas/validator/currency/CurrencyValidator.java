package com.github.denegas.validator.currency;

import com.github.denegas.entity.Currency;
import com.github.denegas.validator.Validator;

public interface CurrencyValidator extends Validator<Currency> {
    boolean isValidCurrencyCode(String code);
    String validateCode(String code);
}
