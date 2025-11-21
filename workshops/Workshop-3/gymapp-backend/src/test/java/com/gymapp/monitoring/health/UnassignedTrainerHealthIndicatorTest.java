package com.gymapp.monitoring.health;

import com.gymapp.model.Trainer;
import com.gymapp.repository.TrainerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnassignedTrainerHealthIndicatorTest {

    @Test
    @DisplayName("Should return DOWN when no unassigned trainers")
    void healthShouldBeDownWhenNoUnassignedTrainers() {
        TrainerRepository mockRepo = mock(TrainerRepository.class);
        when(mockRepo.findActiveTrainersNotAssignedToAnyTrainee()).thenReturn(Collections.emptyList());

        UnassignedTrainerHealthIndicator indicator = new UnassignedTrainerHealthIndicator(mockRepo);
        Health health = indicator.health();

        assertEquals("DOWN", health.getStatus().getCode());
        assertEquals(0, health.getDetails().get("unassignedActiveTrainers"));
    }

    @Test
    @DisplayName("Should return UP when there are unassigned trainers")
    void healthShouldBeUpWhenUnassignedTrainersExist() {
        TrainerRepository mockRepo = mock(TrainerRepository.class);
        when(mockRepo.findActiveTrainersNotAssignedToAnyTrainee()).thenReturn(List.of(new Trainer(), new Trainer()));

        UnassignedTrainerHealthIndicator indicator = new UnassignedTrainerHealthIndicator(mockRepo);
        Health health = indicator.health();

        assertEquals("UP", health.getStatus().getCode());
        assertEquals(2, health.getDetails().get("unassignedActiveTrainers"));
    }
}