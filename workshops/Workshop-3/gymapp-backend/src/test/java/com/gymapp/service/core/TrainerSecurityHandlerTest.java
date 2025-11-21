package com.gymapp.service.core;

import com.gymapp.dto.request.auth.ChangeLoginRequestDTO;
import com.gymapp.dto.request.auth.LoginRequestDTO;
import com.gymapp.model.Trainer;
import com.gymapp.repository.TrainerRepository;
import com.gymapp.utils.PasswordVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerSecurityHandlerTest {

    private TrainerRepository trainerRepository;
    private PasswordVerifier passwordVerifier;
    private PasswordEncoder passwordEncoder;
    private TrainerSecurityHandler handler;

    @BeforeEach
    void setUp() {
        trainerRepository = mock(TrainerRepository.class);
        passwordVerifier = mock(PasswordVerifier.class);
        passwordEncoder = mock(PasswordEncoder.class);
        handler = new TrainerSecurityHandler(trainerRepository, passwordVerifier, passwordEncoder);
    }

    @Test
    void shouldSupportExistingTrainer() {
        when(trainerRepository.findByUsername("trainer.juan"))
                .thenReturn(Optional.of(new Trainer()));

        assertTrue(handler.supports("trainer.juan"));
    }

    @Test
    void shouldNotSupportUnknownTrainer() {
        when(trainerRepository.findByUsername("unknown.user"))
                .thenReturn(Optional.empty());

        assertFalse(handler.supports("unknown.user"));
    }

    @Test
    void shouldAuthenticateValidTrainer() {
        Trainer trainer = new Trainer();
        trainer.setPassword("hashed");

        LoginRequestDTO dto = new LoginRequestDTO("trainer.juan", "raw");

        when(trainerRepository.findByUsername("trainer.juan")).thenReturn(Optional.of(trainer));
        doNothing().when(passwordVerifier).verifyOrThrow("raw", "hashed");

        assertDoesNotThrow(() -> handler.authenticate(dto));
        verify(passwordVerifier).verifyOrThrow("raw", "hashed");
    }

    @Test
    void shouldThrowWhenTrainerNotFoundDuringAuthentication() {
        LoginRequestDTO dto = new LoginRequestDTO("unknown.user", "pass");

        when(trainerRepository.findByUsername("unknown.user")).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> handler.authenticate(dto));
        assertEquals("Trainer not found", ex.getMessage());
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        Trainer trainer = new Trainer();
        trainer.setPassword("oldHash");

        ChangeLoginRequestDTO dto = new ChangeLoginRequestDTO("trainer.juan", "oldRaw", "newRaw");

        when(trainerRepository.findByUsername("trainer.juan")).thenReturn(Optional.of(trainer));
        doNothing().when(passwordVerifier).verifyOrThrow("oldRaw", "oldHash");
        when(passwordEncoder.encode("newRaw")).thenReturn("newHash");

        handler.changePassword(dto);

        assertEquals("newHash", trainer.getPassword());
        verify(trainerRepository).save(trainer);
    }

    @Test
    void shouldThrowWhenTrainerNotFoundDuringPasswordChange() {
        ChangeLoginRequestDTO dto = new ChangeLoginRequestDTO("unknown.user", "old", "new");

        when(trainerRepository.findByUsername("unknown.user")).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> handler.changePassword(dto));
        assertEquals("Trainer not found", ex.getMessage());
    }
}
