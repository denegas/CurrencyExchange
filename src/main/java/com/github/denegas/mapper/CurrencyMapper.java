package com.github.denegas.mapper;

import com.github.denegas.dto.CurrencyRequestDto;
import com.github.denegas.dto.CurrencyResponseDto;
import com.github.denegas.entity.Currency;

import java.util.List;

public interface CurrencyMapper extends Mapper<CurrencyRequestDto, CurrencyResponseDto, Currency> {
    List<CurrencyResponseDto> toDtoList(List<Currency> currencies);
}
