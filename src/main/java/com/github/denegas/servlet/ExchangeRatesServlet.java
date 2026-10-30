package com.github.denegas.servlet;

import com.github.denegas.dto.ExchangeRateResponseDto;
import com.github.denegas.service.ExchangeRateService;
import com.github.denegas.utils.Json;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private ExchangeRateService service;

    @Override
    public void init() throws ServletException {
        service = (ExchangeRateService) getServletConfig().getServletContext().getAttribute("exchangeRateService");
        if (service == null){
            throw new ServletException("couldn't find exchange rate service");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ExchangeRateResponseDto> dtoRates = service.getAllRates();
        Json.writeJson(resp, HttpServletResponse.SC_OK, dtoRates);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
