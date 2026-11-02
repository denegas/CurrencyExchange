package com.github.denegas.service;

import com.github.denegas.dao.ExchangeRateDao;
import com.github.denegas.dto.ExchangeRequestDto;
import com.github.denegas.dto.ExchangeResponseDto;
import com.github.denegas.entity.ExchangeRate;
import com.github.denegas.exception.NotFoundException;
import com.github.denegas.exception.ValidationException;
import com.github.denegas.validator.exchange.ExchangeValidator;

import java.math.BigDecimal;
import java.util.Optional;

public class ExchangeService {
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
        Optional<ExchangeRate> optionalExchangeRate = exchangeRateDao.findByCodes(baseCurrencyCode, targetCurrencyCode);
        if (optionalExchangeRate.isEmpty()) {
            throw new NotFoundException("couldn't find exchangeRate for exchange");
        }
        ExchangeRate exchangeRate = optionalExchangeRate.get();
        BigDecimal rate = exchangeRate.getRate();
        BigDecimal amount = requestDto.amount();
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
