package com.github.denegas.controller.servlet;


import com.github.denegas.model.dto.CurrencyResponseDto;
import com.github.denegas.model.service.CurrencyService;
import com.github.denegas.controller.utils.JsonUtils;
import com.github.denegas.controller.utils.RequestUtils;
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
        String currencyCode = RequestUtils.normalizePath(req.getPathInfo());
        CurrencyResponseDto currencyResponseDto = service.findByCode(currencyCode);
        JsonUtils.writeJson(resp, HttpServletResponse.SC_OK, currencyResponseDto);
    }

}
