package com.github.denegas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "fullName", "code", "sign"})
public record CurrencyDto(Long id,
                          String code,
                          @JsonProperty("name")
                          String fullName,
                          String sign) {
    public CurrencyDto(String code, String fullName, String sign) {
        this(null, code, fullName, sign);
    }
}
