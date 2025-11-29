package com.gymapp.mapper;

import com.gymapp.dto.request.trainer.CreateTrainerDTO;
import com.gymapp.dto.request.trainer.UpdateTrainerDTO;
import com.gymapp.dto.response.trainer.TrainerCredentialsResponseDTO;
import com.gymapp.dto.response.trainer.TrainerProfileResponseDTO;
import com.gymapp.dto.response.trainer.TrainerResponseDTO;
import com.gymapp.dto.response.trainer.TraineeSummaryResponseDTO;
import com.gymapp.model.Trainer;
import com.gymapp.model.Trainee;
import com.gymapp.model.TrainingType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainerMapper {

    public Trainer toEntity(CreateTrainerDTO dto, TrainingType trainingType) {
        return Trainer.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .trainingType(trainingType)
                .build();
    }

    public void updateEntity(Trainer trainer, UpdateTrainerDTO dto) {
        trainer.setFirstName(dto.firstName());
        trainer.setLastName(dto.lastName());
        trainer.setIsActive(dto.isActive());
    }

    public TrainerCredentialsResponseDTO toCredentialsDTO(Trainer trainer) {
        return new TrainerCredentialsResponseDTO(
                trainer.getUsername(),
                trainer.getPassword()
        );
    }

    public TrainerProfileResponseDTO toProfileDTO(Trainer trainer) {
        return new TrainerProfileResponseDTO(
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getTrainingType().getTrainingTypeName(),
                trainer.getIsActive(),
                toTraineeSummaryList(trainer.getTrainees().stream().toList())
        );
    }

    public TrainerResponseDTO toResponseDTO(Trainer trainer) {
        return new TrainerResponseDTO(
                trainer.getUsername(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getTrainingType().getTrainingTypeName(),
                trainer.getIsActive(),
                toTraineeSummaryList(trainer.getTrainees().stream().toList())
        );
    }

    public List<TraineeSummaryResponseDTO> toTraineeSummaryList(List<Trainee> trainees) {
        return trainees.stream()
                .map(this::toTraineeSummaryDTO)
                .toList();
    }

    public TraineeSummaryResponseDTO toTraineeSummaryDTO(Trainee trainee) {
        return new TraineeSummaryResponseDTO(
                trainee.getUsername(),
                trainee.getFirstName(),
                trainee.getLastName()
        );
    }
}