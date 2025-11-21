package com.gymapp.service.trainee.assignment;

import com.gymapp.model.Trainee;
import com.gymapp.model.Trainer;
import com.gymapp.repository.TraineeRepository;
import com.gymapp.repository.TrainerRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TraineeTrainerAssignmentService {

    private final TrainerRepository trainerRepo;
    private final TraineeRepository traineeRepo;

    public TraineeTrainerAssignmentService(TrainerRepository trainerRepo, TraineeRepository traineeRepo) {
        this.trainerRepo = trainerRepo;
        this.traineeRepo = traineeRepo;
    }

    public void updateTrainerList(Trainee trainee, List<String> trainerUsernames) {
        List<Trainer> newTrainers = trainerRepo.findAllByUsernameIn(trainerUsernames);
        trainee.getTrainers().clear();
        trainee.getTrainers().addAll(newTrainers);
        traineeRepo.save(trainee);
    }
}
