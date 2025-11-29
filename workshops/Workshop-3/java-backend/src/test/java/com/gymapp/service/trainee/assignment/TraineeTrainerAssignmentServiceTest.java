package com.gymapp.service.trainee.assignment;

import com.gymapp.model.Trainee;
import com.gymapp.model.Trainer;
import com.gymapp.repository.TraineeRepository;
import com.gymapp.repository.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class TraineeTrainerAssignmentServiceTest {

    private TrainerRepository trainerRepo;
    private TraineeRepository traineeRepo;
    private TraineeTrainerAssignmentService assignmentService;

    @BeforeEach
    void setUp() {
        trainerRepo = mock(TrainerRepository.class);
        traineeRepo = mock(TraineeRepository.class);
        assignmentService = new TraineeTrainerAssignmentService(trainerRepo, traineeRepo);
    }

    @Test
    void shouldUpdateTrainerListForTrainee() {
        Trainee trainee = Trainee.builder().username("sofia.ramirez").build();
        Trainer t1 = Trainer.builder().username("ana.lopez").build();
        Trainer t2 = Trainer.builder().username("miguel.torres").build();

        when(trainerRepo.findAllByUsernameIn(List.of("ana.lopez", "miguel.torres")))
                .thenReturn(List.of(t1, t2));

        assignmentService.updateTrainerList(trainee, List.of("ana.lopez", "miguel.torres"));

        assert trainee.getTrainers().containsAll(List.of(t1, t2));
        verify(traineeRepo).save(trainee);
    }
}