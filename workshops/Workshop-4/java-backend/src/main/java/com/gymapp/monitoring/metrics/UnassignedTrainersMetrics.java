package com.gymapp.monitoring.metrics;

import com.gymapp.repository.TrainerRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class UnassignedTrainersMetrics {

    private static final String CUSTOM_UNASSIGNED_TRAINERS_TOTAL = "custom_unassigned_trainers_total";

    public UnassignedTrainersMetrics(MeterRegistry registry, TrainerRepository trainerRepository) {
        Gauge.builder(CUSTOM_UNASSIGNED_TRAINERS_TOTAL, trainerRepository,
                        repo -> repo.findActiveTrainersNotAssignedToAnyTrainee().size())
                .description("Número de entrenadores activos sin asignar")
                .register(registry);
    }
}