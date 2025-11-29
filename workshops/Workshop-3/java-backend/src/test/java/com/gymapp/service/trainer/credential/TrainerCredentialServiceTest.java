package com.gymapp.service.trainer.credential;

import com.gymapp.model.Trainer;
import com.gymapp.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TrainerCredentialServiceTest {

    private final TrainerCredentialService service = new TrainerCredentialService();

    @Test
    void shouldGenerateUniqueUsername() {
        User existing = Trainer.builder().username("ana.lopez").build();
        String result = service.generateUsername("Ana", "Lopez", List.of(existing));
        assertEquals("ana.lopez1", result);
    }

    @Test
    void shouldGeneratePasswordOfLength10() {
        String password = service.generatePassword();
        assertNotNull(password);
        assertEquals(10, password.length());
    }
}
