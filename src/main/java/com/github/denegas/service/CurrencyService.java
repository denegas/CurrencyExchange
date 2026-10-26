package com.github.denegas.service;

import com.github.denegas.dao.CurrencyDao;
import com.github.denegas.dto.CurrencyDto;
import com.github.denegas.entity.Currency;
import com.github.denegas.exception.*;
import com.github.denegas.mapper.CurrencyMapper;
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

    public CurrencyDto insert(CurrencyDto currencyDto) {
        Currency currency = currencyMapper.toEntity(currencyDto);
        if (!Validator.isValidCurrency(currency)) {
            throw new InvalidCurrencyException("invalid currency to create");
        }
        Currency createdCurrency = currencyDao.insert(currency);
        return currencyMapper.toDto(createdCurrency);

    }

    public Currency findByCode(String code) {
        if (!Validator.isValidCurrencyCode(code)) {
            throw new InvalidCurrencyCodeException("cannot find invalid code!");
        }
        Optional<Currency> optionalCurrency = currencyDao.findByCode(code);
        if (optionalCurrency.isEmpty()) {
            throw new NotFoundException("couldn't find currency with code: " + code);
        }
        return optionalCurrency.get();

    }

    public List<CurrencyDto> getAll() {
        List<Currency> currencies = currencyDao.getAll();
        if (currencies.isEmpty()) {
            throw new NothingThereException("couldn't find any currency");
        }
        return currencyMapper.toDtoList(currencies);
    }
}
