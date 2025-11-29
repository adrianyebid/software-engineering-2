package com.gymapp.monitoring.metrics;

import com.gymapp.repository.TraineeRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class InactiveTraineesMetrics {

    private static final String CUSTOM_INACTIVE_TRAINEES_TOTAL = "custom_inactive_trainees_total";

    public InactiveTraineesMetrics(MeterRegistry registry, TraineeRepository traineeRepository) {
        Gauge.builder(CUSTOM_INACTIVE_TRAINEES_TOTAL, traineeRepository, TraineeRepository::countInactiveTrainees)
                .description("Número total de trainees inactivos")
                .register(registry);
    }
}
