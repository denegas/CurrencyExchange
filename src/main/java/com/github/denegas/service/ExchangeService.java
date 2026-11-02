package com.github.denegas.service;

import com.github.denegas.dao.ExchangeRateDao;
import com.github.denegas.dto.ExchangeRequestDto;
import com.github.denegas.dto.ExchangeResponseDto;
import com.github.denegas.entity.ExchangeRate;
import com.github.denegas.exception.NotFoundException;
import com.github.denegas.exception.ValidationException;
import com.github.denegas.validator.exchange.ExchangeValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangeService {
    private static final String USD_CODE = "USD";
    private static final int RATE_SCALE = 6;
    private final ExchangeRateDao exchangeRateDao;
    private final ExchangeValidator validator;

    public ExchangeService(ExchangeRateDao exchangeRateDao, ExchangeValidator validator) {
        this.exchangeRateDao = exchangeRateDao;
        this.validator = validator;
    }

    public ExchangeResponseDto exchange(ExchangeRequestDto requestDto) {
        if (!validator.isValid(requestDto)) {
            throw new ValidationException("invalid values for exchange");
        }
        String baseCurrencyCode = requestDto.baseCurrencyCode();
        String targetCurrencyCode = requestDto.targetCurrencyCode();
        BigDecimal rate;
        BigDecimal amount = requestDto.amount();
        ExchangeRate exchangeRate;

        Optional<ExchangeRate> optionalBaseTargetRate = exchangeRateDao.findByCodes(baseCurrencyCode, targetCurrencyCode);
        if (optionalBaseTargetRate.isPresent()) {
            exchangeRate = optionalBaseTargetRate.get();
            rate = exchangeRate.getRate();
        }
        // if didn't find base-target rate
        else {
            Optional<ExchangeRate> optionalTargetBaseRate = exchangeRateDao.findByCodes(targetCurrencyCode, baseCurrencyCode);
            if (optionalTargetBaseRate.isPresent()) {
                exchangeRate = optionalTargetBaseRate.get();
                rate = BigDecimal.ONE.divide(exchangeRate.getRate(), RATE_SCALE, RoundingMode.HALF_EVEN);
            }
            // if didn't find target-base rate
            else {
                Optional<ExchangeRate> optionalUsdBase = exchangeRateDao.findByCodes(USD_CODE, baseCurrencyCode);
                Optional<ExchangeRate> optionalUsdTarget = exchangeRateDao.findByCodes(USD_CODE, targetCurrencyCode);
                if (optionalUsdBase.isPresent() && optionalUsdTarget.isPresent()) {
                    ExchangeRate usdBase = optionalUsdBase.get();
                    ExchangeRate usdTarget = optionalUsdTarget.get();
                    rate = usdTarget.getRate().divide(usdBase.getRate(), RATE_SCALE, RoundingMode.HALF_EVEN);
                    BigDecimal convertedAmount = calculateConvertedAmount(rate, amount);

                    return new ExchangeResponseDto(
                            usdBase.getTargetCurrency(),
                            usdTarget.getTargetCurrency(),
                            rate,
                            amount,
                            convertedAmount
                    );
                }
                //if didn't find usd-base or usd-target rates
                else {
                    throw new NotFoundException("couldn't find exchange rate");
                }
            }
        }


        BigDecimal convertedAmount = calculateConvertedAmount(rate, amount);

        return new ExchangeResponseDto(
                exchangeRate.getBaseCurrency(),
                exchangeRate.getTargetCurrency(),
                rate,
                amount,
                convertedAmount
        );
    }

    private BigDecimal calculateConvertedAmount(BigDecimal rate, BigDecimal amount) {
        return rate.multiply(amount);
    }
}
