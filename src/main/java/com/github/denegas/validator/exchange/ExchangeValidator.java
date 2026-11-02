package com.github.denegas.validator.exchange;

import com.github.denegas.dto.ExchangeRequestDto;
import com.github.denegas.validator.Validator;

import java.math.BigDecimal;

public interface ExchangeValidator extends Validator<ExchangeRequestDto> {
    boolean isValidAmount(BigDecimal amount);
}
