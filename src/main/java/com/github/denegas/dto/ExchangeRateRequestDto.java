package com.github.denegas.dto;

import java.math.BigDecimal;

public record ExchangeRateRequestDto(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
}
