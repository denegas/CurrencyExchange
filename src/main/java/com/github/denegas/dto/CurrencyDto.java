package com.github.denegas.dto;

public record CurrencyDto(Long id, String code, String fullName, String sign) {
    public CurrencyDto(String code, String fullName, String sign) {
        this(null, code, fullName, sign);
    }
}
