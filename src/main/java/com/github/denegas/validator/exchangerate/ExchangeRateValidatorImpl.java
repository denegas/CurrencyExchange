package com.github.denegas.validator.exchangerate;


import com.github.denegas.dto.ExchangeRateRequestDto;
import com.github.denegas.validator.currency.CurrencyValidator;
import com.github.denegas.validator.currency.CurrencyValidatorImpl;

public class ExchangeRateValidatorImpl implements ExchangeRateValidator {
    CurrencyValidator currencyValidator = new CurrencyValidatorImpl();

    @Override
    public boolean isValid(ExchangeRateRequestDto exchangeRate) {
        if (exchangeRate == null || exchangeRate.baseCurrencyCode() == null || exchangeRate.targetCurrencyCode() == null
        || exchangeRate.rate() == null) {
            return false;
        }
        if (isSameCurrencies(exchangeRate.baseCurrencyCode(), exchangeRate.targetCurrencyCode())) {
            return false;
        }

        return currencyValidator.isValidCurrencyCode(exchangeRate.baseCurrencyCode())
                && currencyValidator.isValidCurrencyCode(exchangeRate.targetCurrencyCode());

    }
    private boolean isSameCurrencies(String baseCode, String targetCode){
        return baseCode.equals(targetCode);
    }
}
