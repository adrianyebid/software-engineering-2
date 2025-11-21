package com.gymapp.monitoring.metrics;

import com.gymapp.repository.TraineeRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class InactiveTraineesMetrics {

    public InactiveTraineesMetrics(MeterRegistry registry, TraineeRepository traineeRepository) {
        Gauge.builder("custom_inactive_trainees_total", traineeRepository, TraineeRepository::countInactiveTrainees)
                .description("Número total de trainees inactivos")
                .register(registry);
    }
}
