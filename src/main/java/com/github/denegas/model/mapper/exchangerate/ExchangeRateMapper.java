package com.github.denegas.model.mapper.exchangerate;


import com.github.denegas.model.dto.ExchangeRateResponseDto;
import com.github.denegas.model.entity.ExchangeRate;
import com.github.denegas.model.mapper.ResponseMapper;
import com.github.denegas.model.mapper.currency.CurrencyMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper(uses = CurrencyMapper.class)
public interface ExchangeRateMapper extends ResponseMapper<ExchangeRate, ExchangeRateResponseDto> {
    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);
    List<ExchangeRateResponseDto> toDtoList(List<ExchangeRate> rates);
}
