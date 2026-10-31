package com.github.denegas.service;

import com.github.denegas.dao.ExchangeRateDao;
import com.github.denegas.dto.ExchangeRateRequestDto;
import com.github.denegas.dto.ExchangeRateResponseDto;
import com.github.denegas.entity.Currency;
import com.github.denegas.entity.ExchangeRate;
import com.github.denegas.exception.AlreadyExistsException;
import com.github.denegas.exception.InvalidExchangeRateException;
import com.github.denegas.exception.NotFoundException;
import com.github.denegas.exception.NothingThereException;
import com.github.denegas.mapper.exchangerate.ExchangeRateMapper;
import com.github.denegas.validator.exchangerate.ExchangeRateValidator;

import java.util.List;
import java.util.Optional;

public class ExchangeRateService {
    private final ExchangeRateDao dao;
    private final ExchangeRateMapper mapper;
    private final ExchangeRateValidator validator;

    public ExchangeRateService(ExchangeRateDao dao, ExchangeRateMapper mapper, ExchangeRateValidator validator) {
        this.dao = dao;
        this.mapper = mapper;
        this.validator = validator;
    }

    public List<ExchangeRateResponseDto> getAllRates() {
        List<ExchangeRate> rates = dao.getAll();
        if (rates.isEmpty()) {
            throw new NothingThereException("couldn't find any exchange rate");
        }
        return mapper.toDtoList(rates);
    }

    public ExchangeRateResponseDto insert(ExchangeRateRequestDto exchangeRateRequestDto) {
        if (!validator.isValid(exchangeRateRequestDto)) {
            throw new InvalidExchangeRateException("invalid exchange rate to create");
        }
        String baseCurrencyCode = exchangeRateRequestDto.baseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDto.targetCurrencyCode();

        if (dao.existsRate(baseCurrencyCode, targetCurrencyCode)) {
            throw new AlreadyExistsException("creating exchange rate already exists");
        }
        ExchangeRate creatingRate = new ExchangeRate();
        creatingRate.setRate(exchangeRateRequestDto.rate());
        creatingRate.setBaseCurrency(new Currency(baseCurrencyCode, null, null));
        creatingRate.setTargetCurrency(new Currency(targetCurrencyCode, null, null));

        ExchangeRate createdRate = dao.insert(creatingRate);

        return mapper.toDto(createdRate);
    }

    public ExchangeRateResponseDto findByCodes(ExchangeRateRequestDto requestDto) {
        if (!validator.isValidForFind(requestDto)) {
            throw new InvalidExchangeRateException("invalid exchange rate for finding");
        }
        Optional<ExchangeRate> optionalRate = dao.findByCodes(requestDto.baseCurrencyCode(), requestDto.targetCurrencyCode());
        if (optionalRate.isEmpty()) {
            throw new NotFoundException("not found that exchange rate");
        }
        return mapper.toDto(optionalRate.get());
    }
}
