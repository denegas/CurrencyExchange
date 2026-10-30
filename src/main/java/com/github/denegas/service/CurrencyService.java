package com.github.denegas.service;

import com.github.denegas.dao.CurrencyDao;
import com.github.denegas.dto.CurrencyRequestDto;
import com.github.denegas.dto.CurrencyResponseDto;
import com.github.denegas.entity.Currency;
import com.github.denegas.exception.*;
import com.github.denegas.mapper.currency.CurrencyMapper;
import com.github.denegas.utils.Validator;

import java.util.List;
import java.util.Optional;

public class CurrencyService {
    private final CurrencyDao currencyDao;
    private final CurrencyMapper currencyMapper;

    public CurrencyService(CurrencyDao dao, CurrencyMapper currencyMapper) {
        this.currencyDao = dao;
        this.currencyMapper = currencyMapper;
    }

    public CurrencyResponseDto insert(CurrencyRequestDto currencyRequestDto) {
        Currency currency = currencyMapper.toEntity(currencyRequestDto);
        if (!Validator.isValidCurrency(currency)) {
            throw new InvalidCurrencyException("invalid currency to create");
        }
        Currency createdCurrency = currencyDao.insert(currency);
        return currencyMapper.toDto(createdCurrency);

    }

    public CurrencyResponseDto findByCode(String code) {
        if (!Validator.isValidCurrencyCode(code)) {
            throw new InvalidCurrencyException("invalid currency's code!");
        }
        Optional<Currency> optionalCurrency = currencyDao.findByCode(code);
        if (optionalCurrency.isEmpty()) {
            throw new NotFoundException("couldn't find currency with code: " + code);
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
