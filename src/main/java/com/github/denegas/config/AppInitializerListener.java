package com.github.denegas.config;

import com.github.denegas.dao.CurrencyDao;
import com.github.denegas.dao.ExchangeRateDao;
import com.github.denegas.mapper.currency.CurrencyMapper;
import com.github.denegas.mapper.currency.CurrencyMapperImpl;
import com.github.denegas.mapper.exchangerate.ExchangeRateMapper;
import com.github.denegas.mapper.exchangerate.ExchangeRateMapperImpl;
import com.github.denegas.service.CurrencyService;
import com.github.denegas.service.ExchangeRateService;
import com.github.denegas.validator.currency.CurrencyValidator;
import com.github.denegas.validator.currency.CurrencyValidatorImpl;
import com.github.denegas.validator.exchangerate.ExchangeRateValidator;
import com.github.denegas.validator.exchangerate.ExchangeRateValidatorImpl;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitializerListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        CurrencyDao currencyDao = new CurrencyDao(DatabaseInitializerListener.DB_URL);
        CurrencyMapper currencyMapper = new CurrencyMapperImpl();
        CurrencyValidator currencyValidator = new CurrencyValidatorImpl();
        CurrencyService currencyService = new CurrencyService(currencyDao, currencyMapper, currencyValidator);
        sce.getServletContext().setAttribute("currencyService", currencyService);

        ExchangeRateDao exchangeRateDao = new ExchangeRateDao(DatabaseInitializerListener.DB_URL);
        ExchangeRateMapper exchangeRateMapper = new ExchangeRateMapperImpl();
        ExchangeRateValidator exchangeRateValidator = new ExchangeRateValidatorImpl();
        ExchangeRateService exchangeRateService = new ExchangeRateService(exchangeRateDao, exchangeRateMapper, exchangeRateValidator);
        sce.getServletContext().setAttribute("exchangeRateService", exchangeRateService);
    }
}
