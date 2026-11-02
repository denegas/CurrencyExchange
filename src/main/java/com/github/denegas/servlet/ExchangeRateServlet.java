package com.github.denegas.servlet;

import com.github.denegas.dto.ExchangeRateRequestDto;
import com.github.denegas.dto.ExchangeRateResponseDto;
import com.github.denegas.exception.ValidationException;
import com.github.denegas.service.ExchangeRateService;
import com.github.denegas.utils.Json;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

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
                new ExchangeRateRequestDto(getBaseCurrencyCode(req), getTargetCurrencyCode(req), null);
        ExchangeRateResponseDto responseDto = service.findByCodes(requestDto);

        Json.writeJson(resp, HttpServletResponse.SC_OK, responseDto);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> optionalRateParam = getPatchParam(req, "rate");
        if (optionalRateParam.isEmpty()) {
            throw new ValidationException("bad rate parameter for updating exchangeRate");
        }
        String rateParam = optionalRateParam.get();
        String baseCurrencyCode = getBaseCurrencyCode(req);
        String targetCurrencyCode = getTargetCurrencyCode(req);
        BigDecimal rate;
        if (isInvalid(baseCurrencyCode) || isInvalid(targetCurrencyCode) || isInvalid(rateParam)) {
            throw new ValidationException("bad parameters for updating exchangeRate");
        }
        try {
            rate = new BigDecimal(rateParam);
        } catch (NumberFormatException e) {
            throw new ValidationException("bad rate parameter for updating exchangeRate");
        }
        ExchangeRateRequestDto requestDto = new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, rate);

        ExchangeRateResponseDto responseDto = service.update(requestDto);
        Json.writeJson(resp, HttpServletResponse.SC_OK, responseDto);

    }

    private String getBaseCurrencyCode(HttpServletRequest request) {
        if (request.getPathInfo() == null || request.getPathInfo().replace("/", "").isBlank()) {
            return "";
        }
        return request.getPathInfo().trim().replace("/", "").substring(0, 3).toUpperCase();
    }

    private String getTargetCurrencyCode(HttpServletRequest request) {
        if (request.getPathInfo() == null || request.getPathInfo().replace("/", "").isBlank()) {
            return "";
        }
        return request.getPathInfo().trim().replace("/", "").substring(3).toUpperCase();
    }

    private boolean isInvalid(String param) {
        return param == null || param.isBlank();
    }

    private Optional<String> getPatchParam(HttpServletRequest req, String name) throws IOException {
        req.setCharacterEncoding("UTF-8");

        String body;
        try (BufferedReader reader = req.getReader()) {
            body = reader.lines().collect(Collectors.joining());
        }

        for (String pair : body.split("&")) {
            String[] kv = pair.split("=", 2);
            if (URLDecoder.decode(kv[0], StandardCharsets.UTF_8).equals(name)) {
                if (kv.length > 1) {
                    return Optional.of(URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
                }
                return Optional.of("");
            }
        }
        return Optional.empty();
    }
}
