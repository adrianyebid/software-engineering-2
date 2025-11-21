package com.gymapp.interceptor;

import com.gymapp.dto.request.auth.LoginRequestDTO;
import com.gymapp.service.core.AuthManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

/**
 * Interceptor to handle authentication for incoming HTTP requests.
 * It allows public access to specific endpoints and requires authentication for others.
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    private final AuthManager authManager;

    private static final Set<String> PUBLIC_ENDPOINTS = Set.of(
            "/api/v1/auth/login",
            "/api/v1/trainees",
            "/api/v1/trainers"
    );


    private static final Set<String> SWAGGER_ENDPOINTS = Set.of(
            "/v3/api-docs",
            "/v3/api-docs/",
            "/swagger-ui.html",
            "/swagger-ui/",
            "/swagger-ui/index.html",
            "/api/v1/auth/change-password"
    );

    private static final String HEADER_USERNAME = "X-Username";
    private static final String HEADER_PASSWORD = "X-Password";
    private static final String HTTP_POST = "POST";

    public AuthenticationInterceptor(AuthManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        logger.debug("Intercepting request: {} {}", method, path);

        if (isSwaggerRoute(path)) {
            logger.trace("Swagger route accessed: {}", path);
            return true;
        }

        if (isPublicPost(path, method)) {
            logger.trace("Public endpoint accessed: {} {}", method, path);
            return true;
        }

        String username = request.getHeader(HEADER_USERNAME);
        String password = request.getHeader(HEADER_PASSWORD);

        if (isInvalid(username, password)) {
            logger.warn("Missing or invalid credentials for path: {}", path);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        try {
            authManager.authenticate(new LoginRequestDTO(username, password));
            logger.info("Authentication successful for user: {}", username);
            return true;
        } catch (Exception e) {
            logger.warn("Authentication failed for user: {}", username);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    private boolean isPublicPost(String path, String method) {
        return PUBLIC_ENDPOINTS.stream().anyMatch(path::startsWith) && HTTP_POST.equalsIgnoreCase(method);
    }

    private boolean isInvalid(String username, String password) {
        return username == null || username.isBlank() || password == null || password.isBlank();
    }

    private boolean isSwaggerRoute(String path) {
        return SWAGGER_ENDPOINTS.stream().anyMatch(path::startsWith);
    }
}