package com.github.denegas.model.mapper.currency;

import com.github.denegas.model.dto.CurrencyRequestDto;
import com.github.denegas.model.dto.CurrencyResponseDto;
import com.github.denegas.model.entity.Currency;
import com.github.denegas.model.mapper.RequestMapper;
import com.github.denegas.model.mapper.ResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper
public interface CurrencyMapper extends RequestMapper<Currency, CurrencyRequestDto>,
        ResponseMapper<Currency, CurrencyResponseDto> {

    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);
    List<CurrencyResponseDto> toDtoList(List<Currency> currencies);
}
