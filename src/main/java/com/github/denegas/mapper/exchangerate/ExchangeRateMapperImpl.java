package com.github.denegas.mapper.exchangerate;

import com.github.denegas.dto.CurrencyResponseDto;
import com.github.denegas.dto.ExchangeRateResponseDto;
import com.github.denegas.entity.ExchangeRate;
import com.github.denegas.mapper.currency.CurrencyMapper;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRateMapperImpl implements ExchangeRateMapper {
    CurrencyMapper CURRENCY_MAPPER = CurrencyMapper.INSTANCE;

    @Override
    public List<ExchangeRateResponseDto> toDtoList(List<ExchangeRate> rates) {
        if (rates == null) {
            return new ArrayList<>();
        }
        List<ExchangeRateResponseDto> dtoRates = new ArrayList<>();
        for (var rate : rates) {
            dtoRates.add(toDto(rate));
        }
        return dtoRates;
    }

    @Override
    public ExchangeRateResponseDto toDto(ExchangeRate exchangeRate) {
        if (exchangeRate == null) {
            return null;
        }
        CurrencyResponseDto baseCurrencyDto = CURRENCY_MAPPER.toDto(exchangeRate.getBaseCurrency());
        CurrencyResponseDto targetCurrencyDto = CURRENCY_MAPPER.toDto(exchangeRate.getTargetCurrency());

        return new ExchangeRateResponseDto(exchangeRate.getId(),
                baseCurrencyDto,
                targetCurrencyDto,
                exchangeRate.getRate());
    }

}
