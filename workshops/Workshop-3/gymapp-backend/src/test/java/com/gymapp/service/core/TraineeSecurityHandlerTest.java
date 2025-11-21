package com.gymapp.service.core;

import com.gymapp.dto.request.auth.ChangeLoginRequestDTO;
import com.gymapp.dto.request.auth.LoginRequestDTO;
import com.gymapp.model.Trainee;
import com.gymapp.repository.TraineeRepository;
import com.gymapp.utils.PasswordVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeSecurityHandlerTest {

    private TraineeRepository traineeRepository;
    private PasswordVerifier passwordVerifier;
    private PasswordEncoder passwordEncoder;
    private TraineeSecurityHandler handler;

    @BeforeEach
    void setUp() {
        traineeRepository = mock(TraineeRepository.class);
        passwordVerifier = mock(PasswordVerifier.class);
        passwordEncoder = mock(PasswordEncoder.class);
        handler = new TraineeSecurityHandler(traineeRepository, passwordVerifier, passwordEncoder);
    }

    @Test
    void shouldSupportExistingTrainee() {
        when(traineeRepository.findByUsername("sofia.ramirez"))
                .thenReturn(Optional.of(new Trainee()));

        assertTrue(handler.supports("sofia.ramirez"));
    }

    @Test
    void shouldNotSupportUnknownTrainee() {
        when(traineeRepository.findByUsername("unknown.user"))
                .thenReturn(Optional.empty());

        assertFalse(handler.supports("unknown.user"));
    }

    @Test
    void shouldAuthenticateValidTrainee() {
        Trainee trainee = new Trainee();
        trainee.setPassword("hashed");

        LoginRequestDTO dto = new LoginRequestDTO("sofia.ramirez", "raw");

        when(traineeRepository.findByUsername("sofia.ramirez")).thenReturn(Optional.of(trainee));
        doNothing().when(passwordVerifier).verifyOrThrow("raw", "hashed");

        assertDoesNotThrow(() -> handler.authenticate(dto));
        verify(passwordVerifier).verifyOrThrow("raw", "hashed");
    }

    @Test
    void shouldThrowWhenTraineeNotFoundDuringAuthentication() {
        LoginRequestDTO dto = new LoginRequestDTO("unknown.user", "pass");

        when(traineeRepository.findByUsername("unknown.user")).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> handler.authenticate(dto));
        assertEquals("Trainee not found", ex.getMessage());
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        Trainee trainee = new Trainee();
        trainee.setPassword("oldHash");

        ChangeLoginRequestDTO dto = new ChangeLoginRequestDTO("sofia.ramirez", "oldRaw", "newRaw");

        when(traineeRepository.findByUsername("sofia.ramirez")).thenReturn(Optional.of(trainee));
        doNothing().when(passwordVerifier).verifyOrThrow("oldRaw", "oldHash");
        when(passwordEncoder.encode("newRaw")).thenReturn("newHash");

        handler.changePassword(dto);

        assertEquals("newHash", trainee.getPassword());
        verify(traineeRepository).save(trainee);
    }

    @Test
    void shouldThrowWhenTraineeNotFoundDuringPasswordChange() {
        ChangeLoginRequestDTO dto = new ChangeLoginRequestDTO("unknown.user", "old", "new");

        when(traineeRepository.findByUsername("unknown.user")).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> handler.changePassword(dto));
        assertEquals("Trainee not found", ex.getMessage());
    }
}