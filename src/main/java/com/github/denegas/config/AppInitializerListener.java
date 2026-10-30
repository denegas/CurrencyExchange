package com.github.denegas.config;

import com.github.denegas.dao.CurrencyDao;
import com.github.denegas.mapper.currency.CurrencyMapper;
import com.github.denegas.mapper.currency.CurrencyMapperImpl;
import com.github.denegas.service.CurrencyService;
import com.github.denegas.validator.currency.CurrencyValidator;
import com.github.denegas.validator.currency.CurrencyValidatorImpl;
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
    }
}
