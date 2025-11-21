package com.gymapp.mapper;

import com.gymapp.dto.request.trainee.CreateTraineeDTO;
import com.gymapp.dto.request.trainee.UpdateTraineeDTO;
import com.gymapp.dto.response.trainee.TraineeCredentialsResponseDTO;
import com.gymapp.dto.response.trainee.TraineeProfileResponseDTO;
import com.gymapp.dto.response.trainee.TraineeResponseDTO;
import com.gymapp.dto.response.trainee.TrainerSummaryResponseDTO;
import com.gymapp.model.Trainee;
import com.gymapp.model.Trainer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TraineeMapper {

    public Trainee toEntity(CreateTraineeDTO dto) {
        return Trainee.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .dateOfBirth(dto.dateOfBirth())
                .address(dto.address())
                .build();
    }

    public void updateEntity(Trainee trainee, UpdateTraineeDTO dto) {
        trainee.setFirstName(dto.firstName());
        trainee.setLastName(dto.lastName());
        trainee.setDateOfBirth(dto.dateOfBirth());
        trainee.setAddress(dto.address());
        trainee.setIsActive(dto.isActive());
    }

    public TraineeCredentialsResponseDTO toCredentialsDTO(Trainee trainee) {
        return new TraineeCredentialsResponseDTO(
                trainee.getUsername(),
                trainee.getPassword()
        );
    }

    public TraineeProfileResponseDTO toProfileDTO(Trainee trainee) {
        return new TraineeProfileResponseDTO(
                trainee.getFirstName(),
                trainee.getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.getIsActive(),
                toTrainerSummaryList(trainee.getTrainers().stream().toList())
        );
    }

    public TraineeResponseDTO toResponseDTO(Trainee trainee) {
        return new TraineeResponseDTO(
                trainee.getUsername(),
                trainee.getFirstName(),
                trainee.getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.getIsActive(),
                toTrainerSummaryList(trainee.getTrainers().stream().toList())
        );
    }

    public List<TrainerSummaryResponseDTO> toTrainerSummaryList(List<Trainer> trainers) {
        return trainers.stream()
                .map(this::toTrainerSummaryDTO)
                .toList();
    }

    public TrainerSummaryResponseDTO toTrainerSummaryDTO(Trainer trainer) {
        return new TrainerSummaryResponseDTO(
                trainer.getUsername(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getTrainingType().getTrainingTypeName()
        );
    }
}