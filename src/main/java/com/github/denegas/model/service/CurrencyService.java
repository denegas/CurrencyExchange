package com.github.denegas.model.service;

import com.github.denegas.model.dao.CurrencyDao;
import com.github.denegas.model.dto.CurrencyRequestDto;
import com.github.denegas.model.dto.CurrencyResponseDto;
import com.github.denegas.model.entity.Currency;
import com.github.denegas.exception.*;
import com.github.denegas.model.mapper.currency.CurrencyMapper;
import com.github.denegas.model.validator.currency.CurrencyValidator;

import java.util.List;
import java.util.Optional;

public class CurrencyService {
    private static final CurrencyMapper CURRENCY_MAPPER_INSTANCE = CurrencyMapper.INSTANCE;

    private final CurrencyDao currencyDao;
    private final CurrencyValidator currencyValidator;

    public CurrencyService(CurrencyDao dao, CurrencyValidator validator) {
        this.currencyDao = dao;

        this.currencyValidator = validator;
    }

    public CurrencyResponseDto insert(CurrencyRequestDto currencyRequestDto) {
        Currency currency = CURRENCY_MAPPER_INSTANCE.toEntity(currencyRequestDto);
        currencyValidator.validate(currency);
        Currency createdCurrency = currencyDao.insert(currency);

        return CURRENCY_MAPPER_INSTANCE.toDto(createdCurrency);

    }

    public CurrencyResponseDto findByCode(String code) {
        if (!currencyValidator.isValidCurrencyCode(code)) {
            throw new ValidationException("invalid currency's code!");
        }
        Optional<Currency> optionalCurrency = currencyDao.findByCode(code);
        if (optionalCurrency.isEmpty()) {
            throw new NotFoundException("couldn't find currency with code: " + code);
        }
        Currency currency = optionalCurrency.get();
        return CURRENCY_MAPPER_INSTANCE.toDto(currency);

    }

    public List<CurrencyResponseDto> getAll() {
        List<Currency> currencies = currencyDao.getAll();

        return CURRENCY_MAPPER_INSTANCE.toDtoList(currencies);
    }
}
