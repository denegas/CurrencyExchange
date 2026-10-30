package com.github.denegas.validator.exchangerate;


import com.github.denegas.entity.ExchangeRate;
import com.github.denegas.validator.currency.CurrencyValidator;
import com.github.denegas.validator.currency.CurrencyValidatorImpl;

public class ExchangeRateValidatorImpl implements ExchangeRateValidator {
    CurrencyValidator currencyValidator = new CurrencyValidatorImpl();

    @Override
    public boolean isValid(ExchangeRate exchangeRate) {
        if (exchangeRate == null || exchangeRate.getBaseCurrency() == null || exchangeRate.getTargetCurrency() == null
        || exchangeRate.getRate() == null) {
            return false;
        }
        String baseCurrencyCode = exchangeRate.getBaseCurrency().getCode();
        String targetCurrencyCode = exchangeRate.getTargetCurrency().getCode();
        return currencyValidator.isValidCurrencyCode(baseCurrencyCode)
                && currencyValidator.isValidCurrencyCode(targetCurrencyCode);

    }
}
