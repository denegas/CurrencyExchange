package com.github.denegas.validator.exchange;

import com.github.denegas.dto.ExchangeRequestDto;
import com.github.denegas.exception.ValidationException;
import com.github.denegas.utils.ValidatorUtils;
import com.github.denegas.validator.currency.CurrencyValidator;

import java.math.BigDecimal;

public class ExchangeValidatorImpl implements ExchangeValidator {
    private final CurrencyValidator currencyValidator;

    public ExchangeValidatorImpl(CurrencyValidator currencyValidator) {
        this.currencyValidator = currencyValidator;
    }

    @Override
    public void validate(ExchangeRequestDto requestDto) {
        if (requestDto == null) {
            throw new ValidationException("request body is required");
        }
        String baseCode = requestDto.baseCurrencyCode();
        String targetCode = requestDto.targetCurrencyCode();
        BigDecimal amount = requestDto.amount();

        if (ValidatorUtils.isEmptyParameter(baseCode) || ValidatorUtils.isEmptyParameter(targetCode)) {
            throw new ValidationException("currency codes must be non-empty");
        }
        if (amount == null) {
            throw new ValidationException("amount is required");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("amount must be greater than zero");
        }
        if (!currencyValidator.isValidCurrencyCode(baseCode) || !currencyValidator.isValidCurrencyCode(targetCode)) {
            throw new ValidationException("invalid currencies codes");
        }
    }
}
