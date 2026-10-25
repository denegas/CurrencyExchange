package com.github.denegas.mapper;

import com.github.denegas.dto.CurrencyDto;
import com.github.denegas.entity.Currency;

import java.util.ArrayList;
import java.util.List;

public class CurrencyMapperImpl implements CurrencyMapper {

    @Override
    public List<CurrencyDto> toDtoList(List<Currency> currencies) {
        if (currencies == null) {
            return new ArrayList<>();
        }
        List<CurrencyDto> dtos = new ArrayList<>(currencies.size());
        for (var currency : currencies) {
            dtos.add(toDto(currency));
        }
        return dtos;
    }

    @Override
    public CurrencyDto toDto(Currency entity) {
        if (entity == null) {
            return null;
        }
        return new CurrencyDto(entity.getId(), entity.getCode(), entity.getFullName(), entity.getSign());
    }

    @Override
    public Currency toEntity(CurrencyDto dto) {
        if (dto == null) {
            return null;
        }
        return new Currency(dto.id(), dto.code(), dto.fullName(), dto.sign());
    }

}
