package com.github.denegas.service;

import com.github.denegas.dao.ExchangeRateDao;
import com.github.denegas.dto.ExchangeRateRequestDto;
import com.github.denegas.dto.ExchangeRateResponseDto;
import com.github.denegas.entity.ExchangeRate;
import com.github.denegas.exception.InvalidExchangeRateException;
import com.github.denegas.exception.NothingThereException;
import com.github.denegas.mapper.exchangerate.ExchangeRateMapper;
import com.github.denegas.validator.exchangerate.ExchangeRateValidator;

import java.util.List;

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

//    public ExchangeRateResponseDto insert(ExchangeRateRequestDto exchangeRateRequestDto) {
//        if (!validator.isValid(exchangeRateRequestDto)){
//            throw new InvalidExchangeRateException("invalid exchange rate to create");
//        }
//        ExchangeRate createdRate = dao.insert()
//    }
}
