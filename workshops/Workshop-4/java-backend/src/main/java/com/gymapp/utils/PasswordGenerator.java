package com.gymapp.utils;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PasswordGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private PasswordGenerator() {
        throw new IllegalStateException("Utility class");
    }

    public static String generatePassword(int length) {
        SecureRandom random = new SecureRandom();
        return IntStream.range(0, length)  // genera un stream de 0 a length-1
                .map(i -> random.nextInt(CHARACTERS.length())) // índice aleatorio
                .mapToObj(CHARACTERS::charAt) // convierte índice a char
                .map(Object::toString) // char a String
                .collect(Collectors.joining());
    }
}
