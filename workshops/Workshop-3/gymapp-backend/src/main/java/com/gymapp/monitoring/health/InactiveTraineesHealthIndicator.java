package com.gymapp.monitoring.health;

import com.gymapp.repository.TraineeRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class InactiveTraineesHealthIndicator implements HealthIndicator {

    private final TraineeRepository traineeRepository;
    private static final long INACTIVE_TRAINEES_THRESHOLD = 0L;

    public InactiveTraineesHealthIndicator(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    @Override
    public Health health() {
        long count = traineeRepository.countInactiveTrainees();
        return count == INACTIVE_TRAINEES_THRESHOLD
                ? Health.up().withDetail("inactiveTrainees", INACTIVE_TRAINEES_THRESHOLD).build()
                : Health.down().withDetail("inactiveTrainees", count).build();
    }
}
