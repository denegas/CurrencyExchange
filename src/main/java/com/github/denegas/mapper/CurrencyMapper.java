package com.github.denegas.mapper;

import com.github.denegas.dto.CurrencyDto;
import com.github.denegas.entity.Currency;

import java.util.List;

public interface CurrencyMapper extends Mapper<CurrencyDto, Currency> {
    List<CurrencyDto> toDtoList(List<Currency> currencies);
  //  List<Currency> toEntityList(List<CurrencyDto> currencyDTOs);
}
