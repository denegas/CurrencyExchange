package com.github.denegas.model.validator.currency;

import com.github.denegas.model.entity.Currency;
import com.github.denegas.model.validator.Validator;

public interface CurrencyValidator extends Validator<Currency> {
    boolean isValidCurrencyCode(String code);
}
