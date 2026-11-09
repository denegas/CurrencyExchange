package com.github.denegas.controller.servlet;


import com.github.denegas.model.dto.CurrencyRequestDto;
import com.github.denegas.model.dto.CurrencyResponseDto;
import com.github.denegas.model.service.CurrencyService;
import com.github.denegas.controller.utils.JsonUtils;
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
        JsonUtils.writeJson(resp, HttpServletResponse.SC_OK, currencyResponseDtos);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CurrencyRequestDto creatingCurrency = new CurrencyRequestDto(req.getParameter("code"),
                req.getParameter("name"),
                req.getParameter("sign"));
        CurrencyResponseDto createdCurrency = service.insert(creatingCurrency);
        JsonUtils.writeJson(resp, HttpServletResponse.SC_CREATED, createdCurrency);
    }
}
