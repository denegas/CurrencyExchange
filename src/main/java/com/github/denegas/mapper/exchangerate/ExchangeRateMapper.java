package com.github.denegas.mapper.exchangerate;


import com.github.denegas.dto.ExchangeRateResponseDto;
import com.github.denegas.entity.ExchangeRate;
import com.github.denegas.mapper.ResponseMapper;
import com.github.denegas.mapper.currency.CurrencyMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper(uses = CurrencyMapper.class)
public interface ExchangeRateMapper extends ResponseMapper<ExchangeRate, ExchangeRateResponseDto> {
    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);
    List<ExchangeRateResponseDto> toDtoList(List<ExchangeRate> rates);
}
