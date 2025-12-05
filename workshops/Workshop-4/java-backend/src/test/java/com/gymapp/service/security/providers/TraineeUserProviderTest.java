package com.gymapp.service.security.providers;

import com.gymapp.model.Trainee;
import com.gymapp.model.User;
import com.gymapp.repository.TraineeRepository;
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
@DisplayName("TraineeUserProvider Unit Tests")
class TraineeUserProviderTest {

    @Mock
    private TraineeRepository traineeRepo;

    @InjectMocks
    private TraineeUserProvider traineeUserProvider;

    private static final String USERNAME = "traineeuser";
    private Trainee mockTrainee;

    @BeforeEach
    void setUp() {
        mockTrainee = new Trainee();
        mockTrainee.setUsername(USERNAME);
    }

    @Test
    @DisplayName("findUser should return Trainee when found in repository")
    void findUser_shouldReturnTrainee_whenFound() {
        when(traineeRepo.findByUsername(USERNAME)).thenReturn(Optional.of(mockTrainee));

        Optional<User> result = traineeUserProvider.findUser(USERNAME);

        assertTrue(result.isPresent());
        assertEquals(mockTrainee, result.get());
        assertTrue(result.get() instanceof Trainee);
        verify(traineeRepo).findByUsername(USERNAME);
    }

    @Test
    @DisplayName("findUser should return Optional empty when Trainee is not found")
    void findUser_shouldReturnEmpty_whenNotFound() {
        when(traineeRepo.findByUsername(USERNAME)).thenReturn(Optional.empty());

        Optional<User> result = traineeUserProvider.findUser(USERNAME);

        assertFalse(result.isPresent());
        verify(traineeRepo).findByUsername(USERNAME);
    }
}