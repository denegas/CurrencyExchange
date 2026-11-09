package com.github.denegas.model.service;

import com.github.denegas.model.dao.CurrencyDao;
import com.github.denegas.model.dao.ExchangeRateDao;
import com.github.denegas.model.dto.ExchangeRequestDto;
import com.github.denegas.model.dto.ExchangeResponseDto;
import com.github.denegas.model.entity.Currency;
import com.github.denegas.model.entity.ExchangeRate;
import com.github.denegas.exception.NotFoundException;
import com.github.denegas.model.validator.exchange.ExchangeValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangeService {
    private static final String USD_CODE = "USD";
    private static final int RATE_SCALE = 2;
    private static final int AMOUNT_SCALE = 2;

    private final ExchangeRateDao exchangeRateDao;
    private final CurrencyDao currencyDao;
    private final ExchangeValidator validator;

    public ExchangeService(ExchangeRateDao exchangeRateDao, CurrencyDao currencyDao, ExchangeValidator validator) {
        this.exchangeRateDao = exchangeRateDao;
        this.currencyDao = currencyDao;
        this.validator = validator;
    }

    public ExchangeResponseDto exchange(ExchangeRequestDto requestDto) {
        validator.validate(requestDto);
        String baseCurrencyCode = requestDto.baseCurrencyCode();
        String targetCurrencyCode = requestDto.targetCurrencyCode();

        BigDecimal amount = requestDto.amount().setScale(AMOUNT_SCALE, RoundingMode.HALF_EVEN);
        ExchangeRate rateInfo = getRateInfo(baseCurrencyCode, targetCurrencyCode);
        BigDecimal rate = rateInfo.getRate();
        BigDecimal convertedAmount = calculateConvertedAmount(rate, amount).setScale(AMOUNT_SCALE, RoundingMode.HALF_EVEN);

        return new ExchangeResponseDto(
                rateInfo.getBaseCurrency(),
                rateInfo.getTargetCurrency(),
                rate,
                amount,
                convertedAmount
        );
    }


    private ExchangeRate getRateInfo(String baseCurrencyCode, String targetCurrencyCode) {
        if (isSameCurrencies(baseCurrencyCode, targetCurrencyCode)) {
            return getSameCurrencyRate(baseCurrencyCode);
        }
        Optional<ExchangeRate> optionalBaseTargetRate = exchangeRateDao.findByCodes(baseCurrencyCode, targetCurrencyCode);
        if (optionalBaseTargetRate.isPresent()) {
            return optionalBaseTargetRate.get();
        }

        // if didn't find base-target rate
        Optional<ExchangeRate> optionalTargetBaseRate = getRateByTargetBase(baseCurrencyCode, targetCurrencyCode);
        if (optionalTargetBaseRate.isPresent()) {
            return optionalTargetBaseRate.get();
        }

        // if didn't find target-base rate
        Optional<ExchangeRate> optionalUsdBaseUsdTargetRate = getRateByUsd(baseCurrencyCode, targetCurrencyCode);
        if (optionalUsdBaseUsdTargetRate.isPresent()) {
            return optionalUsdBaseUsdTargetRate.get();
        }
        //if didn't find usd-base or usd-target rates
        throw new NotFoundException("couldn't find exchange rate");

    }

    private boolean isSameCurrencies(String baseCurrencyCode, String targetCurrencyCode) {
        return baseCurrencyCode.equalsIgnoreCase(targetCurrencyCode);
    }

    private ExchangeRate getSameCurrencyRate(String currencyCode) {
        Optional<Currency> optionalSameCurrency = currencyDao.findByCode(currencyCode);
        if (optionalSameCurrency.isEmpty()) {
            throw new NotFoundException("couldn't find currency");
        }
        return new ExchangeRate(
                optionalSameCurrency.get(),
                optionalSameCurrency.get(),
                BigDecimal.ONE
        );
    }

    private Optional<ExchangeRate> getRateByTargetBase(String baseCurrencyCode, String targetCurrencyCode) {
        Optional<ExchangeRate> optionalTargetBaseRate = exchangeRateDao.findByCodes(targetCurrencyCode, baseCurrencyCode);
        if (optionalTargetBaseRate.isPresent()) {
            BigDecimal rate = BigDecimal.ONE.divide(optionalTargetBaseRate.get().getRate(), RATE_SCALE, RoundingMode.HALF_EVEN);
            ExchangeRate targetBaseRate = new ExchangeRate(
                    optionalTargetBaseRate.get().getBaseCurrency(),
                    optionalTargetBaseRate.get().getTargetCurrency(),
                    rate
            );

            return Optional.of(targetBaseRate);
        }
        return Optional.empty();
    }

    private Optional<ExchangeRate> getRateByUsd(String baseCurrencyCode, String targetCurrencyCode) {
        Optional<ExchangeRate> optionalUsdBase = exchangeRateDao.findByCodes(USD_CODE, baseCurrencyCode);
        Optional<ExchangeRate> optionalUsdTarget = exchangeRateDao.findByCodes(USD_CODE, targetCurrencyCode);
        if (optionalUsdBase.isPresent() && optionalUsdTarget.isPresent()) {
            ExchangeRate usdBase = optionalUsdBase.get();
            ExchangeRate usdTarget = optionalUsdTarget.get();
            BigDecimal usdBaseRate = usdBase.getRate();
            BigDecimal usdTargetRate = usdTarget.getRate();

            BigDecimal rate = usdTargetRate.divide(usdBaseRate, RATE_SCALE, RoundingMode.HALF_EVEN);

            ExchangeRate usdBaseUsdTarget = new ExchangeRate(
                    usdBase.getTargetCurrency(),
                    usdTarget.getTargetCurrency(),
                    rate
            );
            return Optional.of(usdBaseUsdTarget);
        }
        return Optional.empty();
    }
    private BigDecimal calculateConvertedAmount(BigDecimal rate, BigDecimal amount) {
        return rate.multiply(amount);
    }
}
