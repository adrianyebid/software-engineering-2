package com.gymapp.service.trainee.credential;

import com.gymapp.model.Trainer;
import com.gymapp.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TraineeCredentialServiceTest {

    private final TraineeCredentialService service = new TraineeCredentialService();

    @Test
    void shouldGenerateUniqueUsername() {
        User existing = Trainer.builder().username("luis.gomez").build();
        String result = service.generateUsername("luis", "gomez", List.of(existing));
        assertEquals("luis.gomez1", result);
    }

    @Test
    void shouldGeneratePasswordOfLength10() {
        String password = service.generatePassword();
        assertNotNull(password);
        assertEquals(10, password.length());
    }
}