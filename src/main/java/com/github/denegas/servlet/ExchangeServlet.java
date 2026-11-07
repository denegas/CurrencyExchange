package com.github.denegas.servlet;

import com.github.denegas.dto.ExchangeRequestDto;
import com.github.denegas.dto.ExchangeResponseDto;
import com.github.denegas.exception.ValidationException;
import com.github.denegas.service.ExchangeService;
import com.github.denegas.utils.Json;
import com.github.denegas.utils.RequestUtils;
import com.github.denegas.utils.ValidatorUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private ExchangeService service;

    @Override
    public void init() throws ServletException {
        service = (ExchangeService) getServletContext().getAttribute("exchangeService");
        if (service == null) {
            throw new ServletException("couldn't find exchange service");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRequestDto requestDto = getRequestExchange(req);
        ExchangeResponseDto responseDto = service.exchange(requestDto);

        Json.writeJson(resp, HttpServletResponse.SC_OK, responseDto);
    }

    private ExchangeRequestDto getRequestExchange(HttpServletRequest req) {
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String amountParam = req.getParameter("amount");

        if (ValidatorUtils.isEmptyParameter(baseCurrencyCode) || ValidatorUtils.isEmptyParameter(targetCurrencyCode) || ValidatorUtils.isEmptyParameter(amountParam)) {
            throw new ValidationException("bad parameters for exchange");
        }
        BigDecimal amount = RequestUtils.convertToBigDecimal(amountParam, "Invalid amount parameter");
        return new ExchangeRequestDto(baseCurrencyCode, targetCurrencyCode, amount);

    }

}
