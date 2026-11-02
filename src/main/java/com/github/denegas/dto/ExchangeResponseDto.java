package com.github.denegas.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.denegas.entity.Currency;

import java.math.BigDecimal;

@JsonPropertyOrder({"baseCurrency", "targetCurrency", "rate", "amount", "convertedAmount"})
public record ExchangeResponseDto(Currency baseCurrency, Currency targetCurrency, BigDecimal rate,
                                  BigDecimal amount, BigDecimal convertedAmount) {
}
