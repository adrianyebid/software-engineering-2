package com.gymapp.monitoring.metrics;

import com.gymapp.repository.TrainerRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class UnassignedTrainersMetrics {

    public UnassignedTrainersMetrics(MeterRegistry registry, TrainerRepository trainerRepository) {
        Gauge.builder("custom_unassigned_trainers_total", trainerRepository,
                        repo -> repo.findActiveTrainersNotAssignedToAnyTrainee().size())
                .description("Número de entrenadores activos sin asignar")
                .register(registry);
    }
}