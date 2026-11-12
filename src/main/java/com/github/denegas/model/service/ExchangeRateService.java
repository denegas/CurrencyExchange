package com.github.denegas.model.service;

import com.github.denegas.exception.ValidationException;
import com.github.denegas.model.dao.ExchangeRateDao;
import com.github.denegas.model.dto.ExchangeRateRequestDto;
import com.github.denegas.model.dto.ExchangeRateResponseDto;
import com.github.denegas.model.entity.Currency;
import com.github.denegas.model.entity.ExchangeRate;
import com.github.denegas.exception.NotFoundException;
import com.github.denegas.model.mapper.exchangerate.ExchangeRateMapper;
import com.github.denegas.model.validator.currency.CurrencyValidator;
import com.github.denegas.model.validator.exchangerate.ExchangeRateValidator;

import java.util.List;
import java.util.Optional;

public class ExchangeRateService {
    private final ExchangeRateMapper mapper;
    private final ExchangeRateDao dao;
    private final ExchangeRateValidator validator;
    private final CurrencyValidator currencyValidator;

    public ExchangeRateService(ExchangeRateDao dao, ExchangeRateValidator validator, ExchangeRateMapper mapper, CurrencyValidator currencyValidator) {
        this.dao = dao;
        this.validator = validator;
        this.currencyValidator = currencyValidator;
        this.mapper = mapper;
    }

    public List<ExchangeRateResponseDto> getAllRates() {
        List<ExchangeRate> rates = dao.getAll();

        return mapper.toDtoList(rates);
    }

    public ExchangeRateResponseDto insert(ExchangeRateRequestDto exchangeRateRequestDto) {
        validator.validate(exchangeRateRequestDto);

        String baseCurrencyCode = exchangeRateRequestDto.baseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDto.targetCurrencyCode();

        ExchangeRate creatingRate = new ExchangeRate(
                new Currency(baseCurrencyCode),
                new Currency(targetCurrencyCode),
                exchangeRateRequestDto.rate()
        );

        ExchangeRate createdRate = dao.insert(creatingRate);

        return mapper.toDto(createdRate);
    }

    public ExchangeRateResponseDto findByCodes(ExchangeRateRequestDto requestDto) {
        if (!currencyValidator.isValidCurrencyCode(requestDto.baseCurrencyCode()) || !currencyValidator.isValidCurrencyCode(requestDto.targetCurrencyCode())) {
            throw new ValidationException("invalid currency's codes in exchange rate");
        }

        Optional<ExchangeRate> optionalRate = dao.findByCodes(requestDto.baseCurrencyCode(), requestDto.targetCurrencyCode());
        if (optionalRate.isEmpty()) {
            throw new NotFoundException("not found that exchange rate");
        }
        return mapper.toDto(optionalRate.get());
    }

    public ExchangeRateResponseDto update(ExchangeRateRequestDto requestDto) {
        validator.validate(requestDto);

        ExchangeRate updatingExchangeRate = new ExchangeRate(
                new Currency(requestDto.baseCurrencyCode()),
                new Currency(requestDto.targetCurrencyCode()),
                requestDto.rate()
        );

        if (!dao.update(updatingExchangeRate)) {
            throw new NotFoundException("not found updating exchange rate");
        }
        Optional<ExchangeRate> updatedRate = dao.findByCodes(requestDto.baseCurrencyCode(), requestDto.targetCurrencyCode());
        if (updatedRate.isEmpty()) {
            throw new NotFoundException("not found updating rate");
        }
        return mapper.toDto(updatedRate.get());

    }
}
