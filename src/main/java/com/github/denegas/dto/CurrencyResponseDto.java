package com.github.denegas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "fullName", "code", "sign"})
public record CurrencyResponseDto(long id, String code,
                                  @JsonProperty("name")
                                  String fullName,
                                  String sign) {
}
