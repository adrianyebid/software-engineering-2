package com.gymapp.service.training.factory;

import com.gymapp.dto.request.training.CreateTrainingDTO;
import com.gymapp.mapper.TrainingMapper;
import com.gymapp.model.Trainee;
import com.gymapp.model.Trainer;
import com.gymapp.model.Training;
import com.gymapp.repository.TraineeRepository;
import com.gymapp.repository.TrainerRepository;
import com.gymapp.repository.TrainingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class TrainingFactory {

    private final TrainingRepository trainingRepo;
    private final TraineeRepository traineeRepo;
    private final TrainerRepository trainerRepo;
    private final TrainingMapper trainingMapper;

    public TrainingFactory(TrainingRepository trainingRepo,
                           TraineeRepository traineeRepo,
                           TrainerRepository trainerRepo,
                           TrainingMapper trainingMapper) {
        this.trainingRepo = trainingRepo;
        this.traineeRepo = traineeRepo;
        this.trainerRepo = trainerRepo;
        this.trainingMapper = trainingMapper;
    }

    public void create(CreateTrainingDTO dto) {
        Trainee trainee = traineeRepo.findByUsername(dto.traineeUsername())
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found"));

        Trainer trainer = trainerRepo.findByUsername(dto.trainerUsername())
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found"));

        Training training = trainingMapper.toEntity(dto, trainee, trainer, trainer.getTrainingType());

        trainingRepo.save(training);
    }
}