package com.github.denegas.service;

import com.github.denegas.dao.CurrencyDao;
import com.github.denegas.dto.CurrencyRequestDto;
import com.github.denegas.dto.CurrencyResponseDto;
import com.github.denegas.entity.Currency;
import com.github.denegas.exception.*;
import com.github.denegas.mapper.currency.CurrencyMapper;
import com.github.denegas.validator.currency.CurrencyValidator;

import java.util.List;
import java.util.Optional;

public class CurrencyService {
    private final CurrencyDao currencyDao;
    private final CurrencyMapper currencyMapper;
    private final CurrencyValidator currencyValidator;

    public CurrencyService(CurrencyDao dao, CurrencyMapper currencyMapper, CurrencyValidator validator) {
        this.currencyDao = dao;
        this.currencyMapper = currencyMapper;
        this.currencyValidator = validator;
    }

    public CurrencyResponseDto insert(CurrencyRequestDto currencyRequestDto) {
        Currency currency = currencyMapper.toEntity(currencyRequestDto);
        if (!currencyValidator.isValid(currency)) {
            throw new InvalidCurrencyException("invalid currency to create");
        }
        if (currencyDao.existsByCode(currency.getCode())) {
            throw new AlreadyExistsException("creating currency already exists");
        }
        Currency createdCurrency = currencyDao.insert(currency);
        return currencyMapper.toDto(createdCurrency);

    }

    public CurrencyResponseDto findByCode(String code) {
        String validCaseCurrencyCode = currencyValidator.validateCode(code);
        if (!currencyValidator.isValidCurrencyCode(validCaseCurrencyCode)) {
            throw new InvalidCurrencyException("invalid currency's code!");
        }
        Optional<Currency> optionalCurrency = currencyDao.findByCode(validCaseCurrencyCode);
        if (optionalCurrency.isEmpty()) {
            throw new NotFoundException("couldn't find currency with code: " + validCaseCurrencyCode);
        }
        Currency currency = optionalCurrency.get();
        return currencyMapper.toDto(currency);

    }

    public List<CurrencyResponseDto> getAll() {
        List<Currency> currencies = currencyDao.getAll();
        if (currencies.isEmpty()) {
            throw new NothingThereException("couldn't find any currency");
        }
        return currencyMapper.toDtoList(currencies);
    }
}
