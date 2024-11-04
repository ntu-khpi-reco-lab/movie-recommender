package com.movie.recommender.user.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// Filter to log incoming requests and outgoing responses
@WebFilter("/*")
public class RequestLoggingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Log request information
        String requestInfo = String.format("Request: %s %s", httpRequest.getMethod(), httpRequest.getRequestURL());
        logger.info(requestInfo);

        // Proceed with the request
        chain.doFilter(request, response);

        // Log response information
        String responseInfo = String.format("Response: %d", httpResponse.getStatus());
        logger.info(responseInfo);
    }

    @Override
    public void destroy() {
        // Cleanup logic, if any
    }
}