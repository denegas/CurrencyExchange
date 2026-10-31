package com.github.denegas.validator.exchangerate;


import com.github.denegas.dto.ExchangeRateRequestDto;
import com.github.denegas.validator.currency.CurrencyValidator;
import com.github.denegas.validator.currency.CurrencyValidatorImpl;

public class ExchangeRateValidatorImpl implements ExchangeRateValidator {
    CurrencyValidator currencyValidator = new CurrencyValidatorImpl();

    @Override
    public boolean isValid(ExchangeRateRequestDto requestDto) {
        if (requestDto == null || requestDto.baseCurrencyCode() == null || requestDto.targetCurrencyCode() == null
                || requestDto.rate() == null) {
            return false;
        }
        if (isSameCurrencies(requestDto.baseCurrencyCode(), requestDto.targetCurrencyCode())) {
            return false;
        }

        return currencyValidator.isValidCurrencyCode(requestDto.baseCurrencyCode())
                && currencyValidator.isValidCurrencyCode(requestDto.targetCurrencyCode());

    }

    public boolean isValidForFind(ExchangeRateRequestDto requestDto) {
        if (requestDto == null || requestDto.baseCurrencyCode() == null || requestDto.targetCurrencyCode() == null) {
            return false;
        }
        boolean isValidBaseCode = currencyValidator.isValidCurrencyCode(requestDto.baseCurrencyCode());
        boolean isValidTargetCode = currencyValidator.isValidCurrencyCode(requestDto.targetCurrencyCode());

        return isValidBaseCode && isValidTargetCode;
    }

    private boolean isSameCurrencies(String baseCode, String targetCode) {
        return baseCode.equals(targetCode);
    }
}
