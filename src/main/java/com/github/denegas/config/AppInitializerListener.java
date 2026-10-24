package com.github.denegas.config;

import com.github.denegas.dao.CurrencyDao;
import com.github.denegas.service.CurrencyService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitializerListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        CurrencyDao currencyDao = new CurrencyDao(DatabaseInitializerListener.DB_URL);
        CurrencyService currencyService = new CurrencyService(currencyDao);
        sce.getServletContext().setAttribute("currencyService", currencyService);
    }
}
