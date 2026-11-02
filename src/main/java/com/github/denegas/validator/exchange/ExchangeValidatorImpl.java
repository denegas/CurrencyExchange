package com.github.denegas.validator.exchange;

import com.github.denegas.dto.ExchangeRequestDto;
import com.github.denegas.validator.currency.CurrencyValidator;
import com.github.denegas.validator.currency.CurrencyValidatorImpl;

public class ExchangeValidatorImpl implements ExchangeValidator {
    private final CurrencyValidator currencyValidator = new CurrencyValidatorImpl();

    @Override
    public boolean isValid(ExchangeRequestDto requestDto) {
        String baseCurrencyCode = requestDto.baseCurrencyCode();
        String targetCurrencyCode = requestDto.targetCurrencyCode();
        if (!currencyValidator.isValidCurrencyCode(baseCurrencyCode) || !currencyValidator.isValidCurrencyCode(targetCurrencyCode)) {
            return false;
        }
        return isValidAmount(requestDto.amount());
    }

    public boolean isValidAmount(Double amount) {
        if (amount == null) {
            return false;
        }
        return amount > 0;
    }
}
