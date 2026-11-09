package com.github.denegas.model.validator.exchangerate;


import com.github.denegas.model.dto.ExchangeRateRequestDto;
import com.github.denegas.exception.ValidationException;
import com.github.denegas.model.utils.ValidatorUtils;
import com.github.denegas.model.validator.currency.CurrencyValidator;

import java.math.BigDecimal;
import java.util.Objects;

public class ExchangeRateValidatorImpl implements ExchangeRateValidator {
    private final CurrencyValidator currencyValidator;

    public ExchangeRateValidatorImpl(CurrencyValidator currencyValidator) {
        this.currencyValidator = currencyValidator;
    }

    @Override
    public void validate(ExchangeRateRequestDto requestDto) {
        if (requestDto == null) {
            throw new ValidationException("request body is required");
        }
        if (ValidatorUtils.isEmptyParameter(requestDto.baseCurrencyCode())
                || ValidatorUtils.isEmptyParameter(requestDto.targetCurrencyCode()) || requestDto.rate() == null) {
            throw new ValidationException("exchange rate parameters should not be empty");
        }
        String baseCode = requestDto.baseCurrencyCode();
        String targetCode = requestDto.targetCurrencyCode();
        BigDecimal rate = requestDto.rate();

        if (rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("rate must be greater than zero");
        }
        if (Objects.equals(baseCode, targetCode)) {
            throw new ValidationException("currencies must be different");
        }
        if (!isValidBaseTargetCodes(baseCode, targetCode)) {
            throw new ValidationException("invalid currencies codes");
        }
    }

    private boolean isValidBaseTargetCodes(String baseCode, String targetCode) {
        return currencyValidator.isValidCurrencyCode(baseCode) && currencyValidator.isValidCurrencyCode(targetCode);
    }

}
