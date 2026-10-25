package com.github.denegas.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.github.denegas.dto.CurrencyDto;
import java.io.IOException;

public final class Json {
    private final static ObjectMapper MAPPER = new ObjectMapper();

    private Json() {

    }

    public static void writeJson(HttpServletResponse response, int statusCode, Object body) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        MAPPER.writeValue(response.getWriter(), body);
    }

    public static CurrencyDto readCurrency(HttpServletRequest request) throws JsonProcessingException, IOException {
       return MAPPER.readValue(request.getInputStream(), CurrencyDto.class);
    }


}
