package com.github.denegas.config;

import com.github.denegas.model.dao.CurrencyDao;
import com.github.denegas.model.dao.ExchangeRateDao;
import com.github.denegas.model.service.CurrencyService;
import com.github.denegas.model.service.ExchangeRateService;
import com.github.denegas.model.service.ExchangeService;
import com.github.denegas.model.validator.currency.CurrencyValidator;
import com.github.denegas.model.validator.currency.CurrencyValidatorImpl;
import com.github.denegas.model.validator.exchange.ExchangeValidator;
import com.github.denegas.model.validator.exchange.ExchangeValidatorImpl;
import com.github.denegas.model.validator.exchangerate.ExchangeRateValidator;
import com.github.denegas.model.validator.exchangerate.ExchangeRateValidatorImpl;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitializerListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        CurrencyDao currencyDao = new CurrencyDao(DatabaseInitializerListener.DB_URL);
        CurrencyValidator currencyValidator = new CurrencyValidatorImpl();
        CurrencyService currencyService = new CurrencyService(currencyDao, currencyValidator);
        sce.getServletContext().setAttribute("currencyService", currencyService);

        ExchangeRateDao exchangeRateDao = new ExchangeRateDao(DatabaseInitializerListener.DB_URL);
        ExchangeRateValidator exchangeRateValidator = new ExchangeRateValidatorImpl(currencyValidator);
        ExchangeRateService exchangeRateService = new ExchangeRateService(exchangeRateDao,  exchangeRateValidator, currencyValidator);
        sce.getServletContext().setAttribute("exchangeRateService", exchangeRateService);

        ExchangeValidator exchangeValidator = new ExchangeValidatorImpl(currencyValidator);
        ExchangeService exchangeService = new ExchangeService(exchangeRateDao, currencyDao, exchangeValidator);
        sce.getServletContext().setAttribute("exchangeService", exchangeService);
    }
}
