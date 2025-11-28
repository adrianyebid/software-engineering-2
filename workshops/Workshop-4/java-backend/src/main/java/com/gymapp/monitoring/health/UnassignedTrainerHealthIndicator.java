package com.gymapp.monitoring.health;

import com.gymapp.model.Trainer;
import com.gymapp.repository.TrainerRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UnassignedTrainerHealthIndicator implements HealthIndicator {

    private static final String UNASSIGNED_ACTIVE_TRAINERS = "**unassignedActiveTrainers**";
    private final TrainerRepository trainerRepository;
    private static final int NO_UNASSIGNED_TRAINERS = 0;

    public UnassignedTrainerHealthIndicator(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Override
    public Health health() {
        List<Trainer> unassigned = trainerRepository.findActiveTrainersNotAssignedToAnyTrainee();
        boolean hasUnassigned = !unassigned.isEmpty();

        return hasUnassigned
                ? Health.up().withDetail(UNASSIGNED_ACTIVE_TRAINERS, unassigned.size()).build()
                : Health.down().withDetail(UNASSIGNED_ACTIVE_TRAINERS, NO_UNASSIGNED_TRAINERS).build();
    }
}