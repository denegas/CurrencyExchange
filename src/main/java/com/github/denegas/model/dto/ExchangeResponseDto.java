package com.github.denegas.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.denegas.model.entity.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;

@JsonPropertyOrder({"baseCurrency", "targetCurrency", "rate", "amount", "convertedAmount"})
public record ExchangeResponseDto(Currency baseCurrency, Currency targetCurrency, BigDecimal rate,
                                  BigDecimal amount, BigDecimal convertedAmount) {
    public ExchangeResponseDto {
        amount = amount.setScale(2, RoundingMode.HALF_EVEN);
        convertedAmount = convertedAmount.setScale(2, RoundingMode.HALF_EVEN);

    }
}
