package com.github.denegas.mapper.currency;

import com.github.denegas.dto.CurrencyRequestDto;
import com.github.denegas.dto.CurrencyResponseDto;
import com.github.denegas.entity.Currency;
import com.github.denegas.mapper.RequestMapper;
import com.github.denegas.mapper.ResponseMapper;

import java.util.List;

public interface CurrencyMapper extends RequestMapper<Currency, CurrencyRequestDto>,
        ResponseMapper<Currency, CurrencyResponseDto> {
    List<CurrencyResponseDto> toDtoList(List<Currency> currencies);
}
