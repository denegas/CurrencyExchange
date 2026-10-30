package com.github.denegas.mapper.currency;

import com.github.denegas.dto.CurrencyRequestDto;
import com.github.denegas.dto.CurrencyResponseDto;
import com.github.denegas.entity.Currency;

import java.util.ArrayList;
import java.util.List;

public class CurrencyMapperImpl implements CurrencyMapper {

    @Override
    public List<CurrencyResponseDto> toDtoList(List<Currency> currencies) {
        if (currencies == null) {
            return new ArrayList<>();
        }
        List<CurrencyResponseDto> dtos = new ArrayList<>(currencies.size());
        for (var currency : currencies) {
            dtos.add(toDto(currency));
        }
        return dtos;
    }

    @Override
    public CurrencyResponseDto toDto(Currency entity) {
        if (entity == null) {
            return null;
        }
        return new CurrencyResponseDto(entity.getId(), entity.getCode(), entity.getFullName(), entity.getSign());
    }

    @Override
    public Currency toEntity(CurrencyRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }
        return new Currency(requestDto.code(), requestDto.fullName(), requestDto.sign());
    }

}
