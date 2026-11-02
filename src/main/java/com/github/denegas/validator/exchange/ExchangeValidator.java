package com.github.denegas.validator.exchange;

import com.github.denegas.dto.ExchangeRequestDto;
import com.github.denegas.validator.Validator;

public interface ExchangeValidator extends Validator<ExchangeRequestDto> {
    boolean isValidAmount(Double amount);
}
