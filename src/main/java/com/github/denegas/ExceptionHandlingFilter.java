package com.github.denegas;

import com.github.denegas.exception.*;
import com.github.denegas.utils.Json;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter("/*")
public class ExceptionHandlingFilter implements Filter {
    private final Logger logger = Logger.getAnonymousLogger();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        try {
            chain.doFilter(request, response);
        } catch (InvalidCurrencyException e) {
            Json.writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("message", e.getMessage()));
        } catch (NotFoundException e) {
            Json.writeJson(resp, HttpServletResponse.SC_NOT_FOUND, Map.of("message", e.getMessage()));
        } catch (NothingThereException e) {
            Json.writeJson(resp, HttpServletResponse.SC_OK, Map.of("message", e.getMessage()));
        } catch (AlreadyExistsException e) {
            Json.writeJson(resp, HttpServletResponse.SC_CONFLICT, Map.of("message", e.getMessage()));
        } catch (DbException e) {
            logger.log(Level.SEVERE, "db error: ", e.getMessage());
            Json.writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Map.of("message", "we have some troubles now"));
        }
    }

}

