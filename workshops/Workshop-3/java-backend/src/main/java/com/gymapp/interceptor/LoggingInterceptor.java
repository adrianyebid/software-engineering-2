package com.gymapp.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Interceptor to log incoming HTTP requests and outgoing responses.
 * It generates a unique transaction ID for each request to trace logs.
 */
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String transactionId = UUID.randomUUID().toString();
        MDC.put("transactionId", transactionId);

        logger.info("Incoming request: {} {}", request.getMethod(), request.getRequestURI());
        logger.debug("Headers: {}", Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(h -> h, request::getHeader)));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        logger.info("Response status: {}", response.getStatus());
        if (ex != null) {
            logger.error("Exception occurred: {}", ex.getMessage());
        }
        MDC.clear();
    }
}