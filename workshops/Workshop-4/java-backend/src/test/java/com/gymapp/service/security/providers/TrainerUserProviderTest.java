package com.gymapp.service.security.providers;

import com.gymapp.model.Trainer;
import com.gymapp.model.User;
import com.gymapp.repository.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TrainerUserProvider Unit Tests")
class TrainerUserProviderTest {

    @Mock
    private TrainerRepository trainerRepo;

    @InjectMocks
    private TrainerUserProvider trainerUserProvider;

    private static final String USERNAME = "traineruser";
    private Trainer mockTrainer;

    @BeforeEach
    void setUp() {
        mockTrainer = new Trainer();
        mockTrainer.setUsername(USERNAME);
    }

    @Test
    @DisplayName("findUser should return Trainer when found in repository")
    void findUser_shouldReturnTrainer_whenFound() {
        when(trainerRepo.findByUsername(USERNAME)).thenReturn(Optional.of(mockTrainer));

        Optional<User> result = trainerUserProvider.findUser(USERNAME);

        assertTrue(result.isPresent());
        assertEquals(mockTrainer, result.get());
        assertTrue(result.get() instanceof Trainer);
        verify(trainerRepo).findByUsername(USERNAME);
    }

    @Test
    @DisplayName("findUser should return Optional empty when Trainer is not found")
    void findUser_shouldReturnEmpty_whenNotFound() {
        when(trainerRepo.findByUsername(USERNAME)).thenReturn(Optional.empty());

        Optional<User> result = trainerUserProvider.findUser(USERNAME);

        assertFalse(result.isPresent());
        verify(trainerRepo).findByUsername(USERNAME);
    }
}