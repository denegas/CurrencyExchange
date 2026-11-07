package com.github.denegas.servlet;

import com.github.denegas.dto.ExchangeRateRequestDto;
import com.github.denegas.dto.ExchangeRateResponseDto;
import com.github.denegas.exception.ValidationException;
import com.github.denegas.service.ExchangeRateService;
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
import java.util.Optional;

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
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRateRequestDto requestDto =
                new ExchangeRateRequestDto(RequestUtils.getBaseCurrencyCodeFromPath(req.getPathInfo()),
                        RequestUtils.getTargetCurrencyCodeFromPath(req.getPathInfo()), null);

        ExchangeRateResponseDto responseDto = service.findByCodes(requestDto);

        Json.writeJson(resp, HttpServletResponse.SC_OK, responseDto);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> optionalRateParam = RequestUtils.getPatchParam(req.getReader(), "rate");
        if (optionalRateParam.isEmpty()) {
            throw new ValidationException("bad rate parameter for updating exchangeRate");
        }
        String baseCurrencyCode = RequestUtils.getBaseCurrencyCodeFromPath(req.getPathInfo());
        String targetCurrencyCode = RequestUtils.getTargetCurrencyCodeFromPath(req.getPathInfo());
        String rateParam = optionalRateParam.get();
        BigDecimal rate = RequestUtils.convertToBigDecimal(rateParam, "bad rate parameter for updating exchangeRate");
        if (ValidatorUtils.isEmptyParameter(baseCurrencyCode) || ValidatorUtils.isEmptyParameter(targetCurrencyCode) || ValidatorUtils.isEmptyParameter(rateParam)) {
            throw new ValidationException("bad parameters for updating exchangeRate");
        }

        ExchangeRateRequestDto requestDto = new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, rate);

        ExchangeRateResponseDto responseDto = service.update(requestDto);
        Json.writeJson(resp, HttpServletResponse.SC_OK, responseDto);

    }


}
