package com.gymapp.service.trainee.query;

import com.gymapp.dto.response.training.TraineeTrainingResponseDTO;
import com.gymapp.mapper.TrainingMapper;
import com.gymapp.repository.TrainingRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static com.gymapp.repository.specification.TrainingSpecifications.*;

@Component
public class TraineeQueryService {

    private final TrainingRepository trainingRepo;
    private final TrainingMapper mapper;

    public TraineeQueryService(TrainingRepository trainingRepo, TrainingMapper mapper) {
        this.trainingRepo = trainingRepo;
        this.mapper = mapper;

    }

    public List<TraineeTrainingResponseDTO> findTrainingsByCriteria(String traineeUsername,
                                                                    LocalDate from,
                                                                    LocalDate to,
                                                                    String trainerName,
                                                                    String trainingTypeName) {
        var spec = Specification.allOf(
                byTraineeUsername(traineeUsername),
                fromDate(from),
                toDate(to),
                byTrainerUsername(trainerName),
                byTrainingType(trainingTypeName)
        );

        return trainingRepo.findAll(spec).stream()
                .map(mapper::toTraineeResponseDTO)
                .toList();
    }
}