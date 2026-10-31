package com.github.denegas.servlet;

import com.github.denegas.dto.ExchangeRateRequestDto;
import com.github.denegas.dto.ExchangeRateResponseDto;
import com.github.denegas.service.ExchangeRateService;
import com.github.denegas.utils.Json;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRateService service;

    @Override
    public void init() throws ServletException {
        service = (ExchangeRateService) getServletConfig().getServletContext().getAttribute("exchangeRateService");
        if (service == null) {
            throw new ServletException("couldn't find exchange rate service");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRateRequestDto requestDto =
                new ExchangeRateRequestDto(getBaseCurrencyCode(req), getTargetCurrencyCode(req), null);
        ExchangeRateResponseDto responseDto = service.findByCodes(requestDto);

        Json.writeJson(resp, HttpServletResponse.SC_OK, responseDto);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPatch(req, resp);
    }

    private String getBaseCurrencyCode(HttpServletRequest request) {
        return request.getPathInfo().trim().replace("/", "").substring(0, 3).toUpperCase();
    }

    private String getTargetCurrencyCode(HttpServletRequest request) {
        return request.getPathInfo().trim().replace("/", "").substring(3).toUpperCase();
    }
}
