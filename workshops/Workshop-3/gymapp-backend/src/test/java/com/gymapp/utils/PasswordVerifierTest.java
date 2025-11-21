package com.gymapp.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PasswordVerifierTest {

    private PasswordVerifier verifier;
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();
        verifier = new PasswordVerifier(encoder);
    }

    @Test
    void shouldValidateCorrectPassword() {
        String raw = "secure123";
        String encoded = encoder.encode(raw);
        assertTrue(verifier.isValid(raw, encoded));
    }

    @Test
    void shouldRejectIncorrectPassword() {
        String raw = "secure123";
        String encoded = encoder.encode("wrongpass");
        assertFalse(verifier.isValid(raw, encoded));
    }

    @Test
    void shouldThrowExceptionOnInvalidPassword() {
        String raw = "secure123";
        String encoded = encoder.encode("wrongpass");
        assertThrows(IllegalArgumentException.class, () -> verifier.verifyOrThrow(raw, encoded));
    }
}