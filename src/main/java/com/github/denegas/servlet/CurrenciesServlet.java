package com.github.denegas.servlet;


import com.github.denegas.dto.CurrencyRequestDto;
import com.github.denegas.dto.CurrencyResponseDto;
import com.github.denegas.service.CurrencyService;
import com.github.denegas.utils.Json;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet({"/currencies", "/currencies/"})
public class CurrenciesServlet extends HttpServlet {
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
        List<CurrencyResponseDto> currencyResponseDtos = service.getAll();
        Json.writeJson(resp, HttpServletResponse.SC_OK, currencyResponseDtos);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CurrencyRequestDto creatingCurrency = new CurrencyRequestDto(req.getParameter("code"),
                req.getParameter("fullName"),
                req.getParameter("sign"));
        CurrencyResponseDto createdCurrency = service.insert(creatingCurrency);
        Json.writeJson(resp, HttpServletResponse.SC_CREATED, createdCurrency);
    }
}
