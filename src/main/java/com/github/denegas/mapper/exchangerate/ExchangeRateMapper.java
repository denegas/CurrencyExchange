package com.github.denegas.mapper.exchangerate;


import com.github.denegas.dto.ExchangeRateResponseDto;
import com.github.denegas.entity.ExchangeRate;
import com.github.denegas.mapper.ResponseMapper;

import java.util.List;

public interface ExchangeRateMapper extends ResponseMapper<ExchangeRate, ExchangeRateResponseDto> {
    List<ExchangeRateResponseDto> toDtoList(List<ExchangeRate> rates);
}
