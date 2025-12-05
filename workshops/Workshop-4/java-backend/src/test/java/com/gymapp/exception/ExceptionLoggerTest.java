package com.gymapp.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExceptionLogger Utility Class Tests")
class ExceptionLoggerTest {

    @Mock
    private HttpServletRequest request;

    private static final String URI = "/test/resource";

    private Logger mockLogger;

    @BeforeEach
    void setUp() {

        when(request.getRequestURI()).thenReturn(URI);

        mockLogger = mock(Logger.class);

        ExceptionLogger.setLogger(mockLogger);
    }

    @Test
    void logValidationError_shouldLogWarn() {
        Map<String, String> errors = Map.of("field", "error");

        ExceptionLogger.logValidationError(request, errors);

        verify(mockLogger).warn("Validation error at {}: {}", URI, errors);
    }

    @Test
    void logEntityNotFound_shouldLogWarn() {
        String message = "Not found";

        ExceptionLogger.logEntityNotFound(request, message);

        verify(mockLogger).warn("Entity not found at {}: {}", URI, message);
    }

    @Test
    void logDataConflict_shouldLogError() {
        String message = "Conflict occurred";

        ExceptionLogger.logDataConflict(request, message);

        verify(mockLogger).error("Data integrity violation at {}: {}", URI, message);
    }

    @Test
    void logBadRequest_shouldLogWarn() {
        String message = "Bad request";

        ExceptionLogger.logBadRequest(request, message);

        verify(mockLogger).warn("Bad request at {}: {}", URI, message);
    }

    @Test
    void logUnexpectedError_shouldLogErrorWithException() {
        Exception ex = new RuntimeException("Unexpected failure");

        ExceptionLogger.logUnexpectedError(request, ex);

        verify(mockLogger).error(
                "Unexpected error at {}: {}",
                URI,
                ex.getMessage(),
                ex
        );
    }

    @Test
    void logAuthFailure_shouldLogWarn() {
        String message = "Bad credentials";

        ExceptionLogger.logAuthFailure(request, message);

        verify(mockLogger).warn("Authentication failed at {}: {}", URI, message);
    }

    @Test
    void logUnauthorizedAccess_shouldLogWarn() {
        String message = "Token expired";

        ExceptionLogger.logUnauthorizedAccess(request, message);

        verify(mockLogger).warn("Unauthorized access at {}: {}", URI, message);
    }

    @Test
    void logTooManyRequests_shouldLogWarn() {
        String message = "Brute force attempt";

        ExceptionLogger.logTooManyRequests(request, message);

        verify(mockLogger).warn("Too many requests (Brute Force) at {}: {}", URI, message);
    }
}