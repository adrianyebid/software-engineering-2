package com.gymapp.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest {

    @Test
    void shouldGeneratePasswordOfGivenLength() {
        int length = 12;
        String password = PasswordGenerator.generatePassword(length);
        assertNotNull(password);
        assertEquals(length, password.length());
    }

    @Test
    void shouldGenerateDifferentPasswordsEachTime() {
        String p1 = PasswordGenerator.generatePassword(10);
        String p2 = PasswordGenerator.generatePassword(10);
        assertNotEquals(p1, p2); // Probabilístico, pero útil
    }

    @Test
    void shouldHavePrivateConstructor() throws Exception {
        var constructor = PasswordGenerator.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        Throwable cause = exception.getCause();

        assertInstanceOf(IllegalStateException.class, cause);
        assertEquals("Utility class", cause.getMessage());
    }


}