package com.github.denegas.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.denegas.model.entity.Currency;

import java.math.BigDecimal;

@JsonPropertyOrder({"baseCurrency", "targetCurrency", "rate", "amount", "convertedAmount"})
public record ExchangeResponseDto(Currency baseCurrency, Currency targetCurrency, BigDecimal rate,
                                  BigDecimal amount, BigDecimal convertedAmount) {
}
