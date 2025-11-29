package com.gymapp.mapper;

import com.gymapp.dto.request.training.CreateTrainingDTO;
import com.gymapp.dto.response.training.TraineeTrainingResponseDTO;
import com.gymapp.dto.response.training.TrainerTrainingResponseDTO;
import com.gymapp.model.Training;
import com.gymapp.model.Trainer;
import com.gymapp.model.Trainee;
import com.gymapp.model.TrainingType;
import org.springframework.stereotype.Component;

@Component
public class TrainingMapper {

    public Training toEntity(CreateTrainingDTO dto, Trainee trainee, Trainer trainer, TrainingType trainingType) {
        return Training.builder()
                .trainingName(dto.trainingName())
                .trainingDate(dto.trainingDate())
                .trainingDuration(dto.trainingDuration())
                .trainee(trainee)
                .trainer(trainer)
                .trainingType(trainingType)
                .build();
    }

    public TraineeTrainingResponseDTO toTraineeResponseDTO(Training training) {
        return new TraineeTrainingResponseDTO(
                training.getTrainingName(),
                training.getTrainingDate(),
                training.getTrainingType().getTrainingTypeName(),
                training.getTrainingDuration(),
                training.getTrainer().getFirstName() + " " + training.getTrainer().getLastName()
        );
    }

    public TrainerTrainingResponseDTO toTrainerResponseDTO(Training training) {
        return new TrainerTrainingResponseDTO(
                training.getTrainingName(),
                training.getTrainingDate(),
                training.getTrainingType().getTrainingTypeName(),
                training.getTrainingDuration(),
                training.getTrainee().getFirstName() + " " + training.getTrainee().getLastName()
        );
    }
}