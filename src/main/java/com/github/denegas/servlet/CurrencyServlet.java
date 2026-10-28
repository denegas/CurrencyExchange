package com.github.denegas.servlet;


import com.github.denegas.dto.CurrencyResponseDto;
import com.github.denegas.service.CurrencyService;
import com.github.denegas.utils.Json;
import com.github.denegas.utils.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrencyService service;

    @Override
    public void init() throws ServletException {
        this.service = (CurrencyService) getServletContext().getAttribute("currencyService");
        if (service == null) {
            throw new ServletException("service is not found");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String validCode = getCurrencyCodeFromPath(req);
       // CurrencyDto currencyDto = service.findByCode(validCode);
        CurrencyResponseDto currencyResponseDto = service.findByCode(validCode);
        Json.writeJson(resp, HttpServletResponse.SC_OK, currencyResponseDto);
    }

    private String getCurrencyCodeFromPath(HttpServletRequest request) {
        String code = request.getPathInfo();
        return Validator.validateCode(code);
    }

}
