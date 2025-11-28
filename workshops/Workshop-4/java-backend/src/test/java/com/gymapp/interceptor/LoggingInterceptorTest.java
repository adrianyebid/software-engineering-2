package com.gymapp.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoggingInterceptorTest {

    @Test
    @DisplayName("Should set transactionId and return true in preHandle")
    void preHandleShouldSetTransactionIdAndReturnTrue() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Object handler = new Object();

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/test");
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(Collections.emptyList()));

        LoggingInterceptor interceptor = new LoggingInterceptor();

        // Act
        boolean result = interceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        assertNotNull(MDC.get("transactionId"));
    }

    @Test
    @DisplayName("Should log response and clear MDC in afterCompletion")
    void afterCompletionShouldClearMDC() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Object handler = new Object();

        when(response.getStatus()).thenReturn(200);

        MDC.put("transactionId", "test-id");

        LoggingInterceptor interceptor = new LoggingInterceptor();

        // Act
        interceptor.afterCompletion(request, response, handler, null);

        // Assert
        assertNull(MDC.get("transactionId"));
    }

    @Test
    @DisplayName("Should log exception if present in afterCompletion")
    void afterCompletionShouldLogException() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Object handler = new Object();
        Exception ex = new RuntimeException("Something went wrong");

        when(response.getStatus()).thenReturn(500);

        MDC.put("transactionId", "test-id");

        LoggingInterceptor interceptor = new LoggingInterceptor();

        // Act
        interceptor.afterCompletion(request, response, handler, ex);

        // Assert
        assertNull(MDC.get("transactionId")); // MDC should still be cleared
    }
}