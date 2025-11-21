package com.gymapp.utils;

import com.gymapp.model.Trainer;
import com.gymapp.model.User;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsernameGeneratorTest {

    @Test
    void shouldGenerateBaseUsernameIfUnique() {
        List<User> existing = List.of();
        String username = UsernameGenerator.generateUsername("luis", "gomez", existing);
        assertEquals("luis.gomez", username);
    }

    @Test
    void shouldAppendSuffixIfUsernameExists() {
        User existingUser = Trainer.builder().username("luis.gomez").build();
        List<User> existing = List.of(existingUser);
        String username = UsernameGenerator.generateUsername("luis", "gomez", existing);
        assertEquals("luis.gomez1", username);
    }

    @Test
    void shouldSkipMultipleConflicts() {
        List<User> existing = List.of(
                Trainer.builder().username("luis.gomez").build(),
                Trainer.builder().username("luis.gomez1").build(),
                Trainer.builder().username("luis.gomez2").build()
        );
        String username = UsernameGenerator.generateUsername("luis", "gomez", existing);
        assertEquals("luis.gomez3", username);
    }

    @Test
    void shouldThrowExceptionOnInstantiation() throws Exception {
        var constructor = UsernameGenerator.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertInstanceOf(IllegalStateException.class, exception.getCause());
        assertEquals("Utility class", exception.getCause().getMessage());
    }
}