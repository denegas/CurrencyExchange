package com.github.denegas.dto;

public record ExchangeRequestDto(String baseCurrencyCode, String targetCurrencyCode, double amount) {
}
