package com.github.denegas.controller.filter;

import com.github.denegas.exception.*;
import com.github.denegas.controller.utils.JsonUtils;
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
        } catch (ValidationException e) {
            JsonUtils.writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("message", e.getMessage()));
        } catch (NotFoundException e) {
            JsonUtils.writeJson(resp, HttpServletResponse.SC_NOT_FOUND, Map.of("message", e.getMessage()));
        }  catch (AlreadyExistsException e) {
            JsonUtils.writeJson(resp, HttpServletResponse.SC_CONFLICT, Map.of("message", e.getMessage()));
        } catch (DbException e) {
            logger.log(Level.SEVERE, "db error: ", e);
            JsonUtils.writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Map.of("message", "we have some troubles now"));
        }
    }

}

