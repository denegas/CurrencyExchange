package com.github.denegas.config;

import com.github.denegas.dao.CurrencyDao;
import com.github.denegas.mapper.CurrencyMapper;
import com.github.denegas.mapper.CurrencyMapperImpl;
import com.github.denegas.service.CurrencyService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitializerListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        CurrencyDao currencyDao = new CurrencyDao(DatabaseInitializerListener.DB_URL);
        CurrencyMapper currencyMapper = new CurrencyMapperImpl();
        CurrencyService currencyService = new CurrencyService(currencyDao, currencyMapper);
        sce.getServletContext().setAttribute("currencyService", currencyService);
    }
}
