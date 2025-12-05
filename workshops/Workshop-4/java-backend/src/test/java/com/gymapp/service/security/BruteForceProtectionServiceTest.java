package com.gymapp.service.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BruteForceProtectionService Unit Tests")
class BruteForceProtectionServiceTest {

    private BruteForceProtectionService service;
    private static final String TEST_KEY = "testuser";
    private static final int MAX_ATTEMPT = 5;

    @BeforeEach
    void setUp() {
        // Inicializar el servicio para cada prueba, asegurando aislamiento.
        service = new BruteForceProtectionService();
    }

    @Test
    void isBlocked_shouldReturnFalseInitially() {
        assertFalse(service.isBlocked(TEST_KEY));
    }

    @Test
    void loginFailed_shouldCountAttempts() {

        for (int i = 0; i < MAX_ATTEMPT - 1; i++) {
            service.loginFailed(TEST_KEY);
            assertFalse(service.isBlocked(TEST_KEY));
        }

        service.loginFailed(TEST_KEY);
        assertTrue(service.isBlocked(TEST_KEY));
    }

    @Test
    void loginFailed_shouldLockUserAfterMaxAttempts() {
        for (int i = 0; i < MAX_ATTEMPT; i++) {
            service.loginFailed(TEST_KEY);
        }
        assertTrue(service.isBlocked(TEST_KEY));
    }

    @Test
    void loginSucceeded_shouldClearAttemptsBeforeLock() {

        for (int i = 0; i < MAX_ATTEMPT - 1; i++) {
            service.loginFailed(TEST_KEY);
        }

        service.loginSucceeded(TEST_KEY);

        service.loginFailed(TEST_KEY);
        assertFalse(service.isBlocked(TEST_KEY));
    }

    @Test
    void loginSucceeded_shouldUnlockUser() {

        for (int i = 0; i < MAX_ATTEMPT; i++) {
            service.loginFailed(TEST_KEY);
        }
        assertTrue(service.isBlocked(TEST_KEY));
        service.loginSucceeded(TEST_KEY);
        assertFalse(service.isBlocked(TEST_KEY));
    }

}