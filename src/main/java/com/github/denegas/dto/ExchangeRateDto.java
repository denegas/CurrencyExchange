package com.github.denegas.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "baseCurrency", "targetCurrency", "rate"})
public record ExchangeRateDto(Long id, CurrencyDto baseCurrency, CurrencyDto targetCurrency, double rate) {
    public ExchangeRateDto(CurrencyDto baseCurrency, CurrencyDto targetCurrency, double rate) {
        this(null, baseCurrency, targetCurrency, rate);
    }
}
