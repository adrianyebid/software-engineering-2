package com.gymapp.monitoring.health;

import com.gymapp.repository.TraineeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InactiveTraineesHealthIndicatorTest {

    @Test
    @DisplayName("Should return UP when no inactive trainees")
    void healthShouldBeUpWhenNoInactiveTrainees() {
        TraineeRepository mockRepo = mock(TraineeRepository.class);
        when(mockRepo.countInactiveTrainees()).thenReturn(0L);

        InactiveTraineesHealthIndicator indicator = new InactiveTraineesHealthIndicator(mockRepo);
        Health health = indicator.health();

        assertEquals("UP", health.getStatus().getCode());
        assertEquals(0L, health.getDetails().get("**inactiveTrainees**"));
    }

    @Test
    @DisplayName("Should return DOWN when there are inactive trainees")
    void healthShouldBeDownWhenInactiveTraineesExist() {
        TraineeRepository mockRepo = mock(TraineeRepository.class);
        when(mockRepo.countInactiveTrainees()).thenReturn(3L);

        InactiveTraineesHealthIndicator indicator = new InactiveTraineesHealthIndicator(mockRepo);
        Health health = indicator.health();

        assertEquals("DOWN", health.getStatus().getCode());
        assertEquals(3L, health.getDetails().get("**inactiveTrainees**"));
    }
}