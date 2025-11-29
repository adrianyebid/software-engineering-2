package com.gymapp.utils;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordVerifier {

    private final PasswordEncoder passwordEncoder;

    public PasswordVerifier(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isValid(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public void verifyOrThrow(String rawPassword, String encodedPassword) {
        if (isValid(rawPassword, encodedPassword)) {
            return;
        }
        throw new IllegalArgumentException("Contraseña incorrecta");
    }
}
