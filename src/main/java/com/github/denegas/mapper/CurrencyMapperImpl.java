package com.github.denegas.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.denegas.dto.CurrencyDto;
import com.github.denegas.entity.Currency;

import java.util.ArrayList;
import java.util.List;

public class CurrencyMapperImpl implements CurrencyMapper {
    private final static ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public List<CurrencyDto> toDtoList(List<Currency> currencies) {
        List<CurrencyDto> dtos = new ArrayList<>();
        for (var currency : currencies) {
            dtos.add(toDto(currency));
        }
        return dtos;
    }

    @Override
    public CurrencyDto toDto(Currency entity) {
        return new CurrencyDto(entity.code(), entity.fullName(), entity.sign());
    }

    @Override
    public Currency toEntity(CurrencyDto dto) {
        return new Currency(dto.code(), dto.fullName(), dto.sign());
    }

}
