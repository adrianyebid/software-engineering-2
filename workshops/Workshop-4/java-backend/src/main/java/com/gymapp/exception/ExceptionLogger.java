package com.gymapp.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ExceptionLogger {

    private static Logger log = LoggerFactory.getLogger(ExceptionLogger.class);

    private ExceptionLogger() {
        throw new IllegalStateException("Utility class");
    }

    static void setLogger(Logger newLogger) {
        log = newLogger;
    }

    public static void logValidationError(HttpServletRequest request, Map<String, String> errors) {
        log.warn("Validation error at {}: {}", request.getRequestURI(), errors);
    }

    public static void logEntityNotFound(HttpServletRequest request, String message) {
        log.warn("Entity not found at {}: {}", request.getRequestURI(), message);
    }

    public static void logDataConflict(HttpServletRequest request, String message) {
        log.error("Data integrity violation at {}: {}", request.getRequestURI(), message);
    }

    public static void logBadRequest(HttpServletRequest request, String message) {
        log.warn("Bad request at {}: {}", request.getRequestURI(), message);
    }

    public static void logUnexpectedError(HttpServletRequest request, Exception ex) {
        log.error("Unexpected error at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
    }


    public static void logAuthFailure(HttpServletRequest request, String message) {
        log.warn("Authentication failed at {}: {}", request.getRequestURI(), message);
    }

    public static void logUnauthorizedAccess(HttpServletRequest request, String message) {
        log.warn("Unauthorized access at {}: {}", request.getRequestURI(), message);
    }

    public static void logTooManyRequests(HttpServletRequest request, String message) {
        log.warn("Too many requests (Brute Force) at {}: {}", request.getRequestURI(), message);
    }
}