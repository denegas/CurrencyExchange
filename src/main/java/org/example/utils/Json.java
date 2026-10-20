package org.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public final class Json {
    private final static ObjectMapper MAPPER = new ObjectMapper();

    private Json() {

    }

    public void writeJson(HttpServletResponse response, int statusCode, Object body) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        MAPPER.writeValue(response.getWriter(), body);
    }



}
