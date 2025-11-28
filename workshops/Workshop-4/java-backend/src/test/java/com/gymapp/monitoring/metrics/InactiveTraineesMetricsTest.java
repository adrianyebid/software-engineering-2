package com.gymapp.monitoring.metrics;

import com.gymapp.repository.TraineeRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InactiveTraineesMetricsTest {

    @Test
    @DisplayName("Should register gauge and reflect inactive trainee count")
    void gaugeShouldReflectInactiveTraineeCount() {
        // Arrange
        TraineeRepository mockRepo = mock(TraineeRepository.class);
        when(mockRepo.countInactiveTrainees()).thenReturn(5L);

        MeterRegistry registry = new SimpleMeterRegistry();

        // Act
        new InactiveTraineesMetrics(registry, mockRepo);

        // Assert
        Double gaugeValue = registry.get("custom_inactive_trainees_total").gauge().value();
        assertEquals(5.0, gaugeValue);
    }
}