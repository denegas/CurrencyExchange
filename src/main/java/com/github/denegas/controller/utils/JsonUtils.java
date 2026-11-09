package com.github.denegas.controller.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class JsonUtils {
    private final static ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {

    }

    public static void writeJson(HttpServletResponse response, int statusCode, Object body) throws IOException {
        response.setContentType("application/json");
        response.setStatus(statusCode);
        MAPPER.writeValue(response.getWriter(), body);
    }



}
