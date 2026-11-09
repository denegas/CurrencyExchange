package com.github.denegas.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;

@JsonPropertyOrder({"id", "baseCurrency", "targetCurrency", "rate"})
public record ExchangeRateResponseDto(long id, CurrencyResponseDto baseCurrency, CurrencyResponseDto targetCurrency, BigDecimal rate) {

}
