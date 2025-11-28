package com.gymapp.exception;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/test");
    }

    @Test
    void shouldHandleValidationException() {
        FieldError error1 = new FieldError("dto", "firstName", "must not be blank");
        FieldError error2 = new FieldError("dto", "password", "must not be null");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(error1, error2));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, Object>> response = handler.handleValidationException(ex, request);

        assertEquals(400, response.getStatusCode().value());
        Map<String, Object> body = response.getBody();
        assertEquals("Validation Error", body.get("error"));
        assertEquals("Campos inválidos", body.get("message"));
        assertEquals("/api/test", body.get("path"));

        Map<String, String> errors = (Map<String, String>) body.get("errors");
        assertEquals("must not be blank", errors.get("firstName"));
        assertEquals("must not be null", errors.get("password"));
    }

    @Test
    void shouldHandleEntityNotFound() {
        EntityNotFoundException ex = new EntityNotFoundException("Trainer not found");

        ResponseEntity<ErrorResponse> response = handler.handleEntityNotFound(ex, request);

        assertEquals(404, response.getStatusCode().value());
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("Resource Not Found", body.error());
        assertEquals("Trainer not found", body.message());
        assertEquals("/api/test", body.path());
    }

    @Test
    void shouldHandleDataConflict() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Duplicate key");

        ResponseEntity<ErrorResponse> response = handler.handleDataConflict(ex, request);

        assertEquals(409, response.getStatusCode().value());
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("Data Conflict", body.error());
        assertEquals("Duplicate key", body.message());
        assertEquals("/api/test", body.path());
    }

    @Test
    void shouldHandleIllegalArgument() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid status");

        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgument(ex, request);

        assertEquals(400, response.getStatusCode().value());
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("Bad Request", body.error());
        assertEquals("Invalid status", body.message());
        assertEquals("/api/test", body.path());
    }

    @Test
    void shouldHandleGenericException() {
        Exception ex = new RuntimeException("Unexpected failure");

        ResponseEntity<ErrorResponse> response = handler.handleGenericException(ex, request);

        assertEquals(500, response.getStatusCode().value());
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("Internal Server Error", body.error());
        assertEquals("An unexpected error occurred", body.message());
        assertEquals("/api/test", body.path());
    }
}