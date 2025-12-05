package com.gymapp.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Unit Tests")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private HttpServletRequest request;

    private static final String REQUEST_URI = "/api/v1/test";
    private MockedStatic<ExceptionLogger> mockedLogger;

    @BeforeEach
    void setUp() {
        when(request.getRequestURI()).thenReturn(REQUEST_URI);
        // Mockear la clase estática ExceptionLogger
        mockedLogger = mockStatic(ExceptionLogger.class);
    }

    @AfterEach
    void tearDown() {
        // Cerrar el mock estático
        mockedLogger.close();
    }

    // ============================================================
    // Security Exception Handlers (401, 429)
    // ============================================================

    @Test
    void handleBadCredentials_shouldReturn401Unauthorized() {
        BadCredentialsException ex = new BadCredentialsException("Bad password");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBadCredentials(ex, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), body.status());
        assertEquals("Invalid credentials", body.message());
        assertEquals(REQUEST_URI, body.path());
        mockedLogger.verify(() -> ExceptionLogger.logAuthFailure(request, ex.getMessage()));
    }

    @Test
    void handleUnauthorized_shouldReturn401Unauthorized() {
        UnauthorizedException ex = new UnauthorizedException("Token is invalid");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUnauthorized(ex, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), body.status());
        assertEquals("Token is invalid", body.message());
        mockedLogger.verify(() -> ExceptionLogger.logUnauthorizedAccess(request, ex.getMessage()));
    }

    @Test
    void handleTooManyRequests_shouldReturn429TooManyRequests() {
        TooManyRequestsException ex = new TooManyRequestsException("User blocked");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleTooManyRequests(ex, request);

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.TOO_MANY_REQUESTS.value(), body.status());
        assertEquals("User blocked", body.message());
        mockedLogger.verify(() -> ExceptionLogger.logTooManyRequests(request, ex.getMessage()));
    }

    // ============================================================
    // Validation Handler (400)
    // ============================================================

    @Test
    void handleValidationException_shouldReturn400BadRequest_withFieldErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError error1 = new FieldError("objectName", "firstName", "First name is required");
        FieldError error2 = new FieldError("objectName", "age", "Age must be positive");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(error1, error2));
        when(bindingResult.getObjectName()).thenReturn("UserDto");

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleValidationException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.get("status"));
        assertEquals("Validation Error", body.get("error"));
        assertEquals("UserDto", body.get("object"));

        Map<String, String> errors = (Map<String, String>) body.get("errors");
        assertEquals(2, errors.size());
        assertEquals("First name is required", errors.get("firstName"));
        assertEquals("Age must be positive", errors.get("age"));

        mockedLogger.verify(() -> ExceptionLogger.logValidationError(eq(request), anyMap()));
    }

    // ============================================================
    // Data/Resource Handlers (404, 409, 400)
    // ============================================================

    @Test
    void handleEntityNotFound_shouldReturn404NotFound() {
        EntityNotFoundException ex = new EntityNotFoundException("Trainee not found with ID 5");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleEntityNotFound(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.NOT_FOUND.value(), body.status());
        assertEquals("Trainee not found with ID 5", body.message());
        mockedLogger.verify(() -> ExceptionLogger.logEntityNotFound(request, ex.getMessage()));
    }

    @Test
    void handleDataConflict_shouldReturn409Conflict() {
        String causeMessage = "Duplicate entry 'testuser' for key 'users.username'";
        DataIntegrityViolationException ex = new DataIntegrityViolationException("SQL Error", new RuntimeException(causeMessage));

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleDataConflict(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.CONFLICT.value(), body.status());
        assertEquals(causeMessage, body.message());
        mockedLogger.verify(() -> ExceptionLogger.logDataConflict(request, causeMessage));
    }

    @Test
    void handleIllegalArgument_shouldReturn400BadRequest() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid date format");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgument(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.status());
        assertEquals("Invalid date format", body.message());
        mockedLogger.verify(() -> ExceptionLogger.logBadRequest(request, ex.getMessage()));
    }

    // ============================================================
    // Generic Handler (500)
    // ============================================================

    @Test
    void handleGenericException_shouldReturn500InternalServerError() {
        Exception ex = new RuntimeException("Database connection failed");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.status());
        assertEquals("An unexpected error occurred", body.message());
        mockedLogger.verify(() -> ExceptionLogger.logUnexpectedError(request, ex));
    }
}