package com.gymapp.monitoring.metrics;

import com.gymapp.model.Trainer;
import com.gymapp.repository.TrainerRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UnassignedTrainersMetricsTest {

    @Test
    @DisplayName("Should register gauge and reflect unassigned trainer count")
    void gaugeShouldReflectUnassignedTrainerCount() {
        // Arrange
        TrainerRepository mockRepo = mock(TrainerRepository.class);
        when(mockRepo.findActiveTrainersNotAssignedToAnyTrainee())
                .thenReturn(List.of(new Trainer(), new Trainer(), new Trainer()));

        MeterRegistry registry = new SimpleMeterRegistry();

        // Act
        new UnassignedTrainersMetrics(registry, mockRepo);

        // Assert
        double gaugeValue = registry.get("custom_unassigned_trainers_total").gauge().value();
        assertEquals(3.0, gaugeValue);
    }
}