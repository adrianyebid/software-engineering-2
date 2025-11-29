package com.gymapp.service.trainer.query;

import com.gymapp.dto.response.training.TrainerTrainingResponseDTO;
import com.gymapp.mapper.TrainingMapper;
import com.gymapp.repository.TrainingRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import static com.gymapp.repository.specification.TrainingSpecifications.*;


import java.time.LocalDate;
import java.util.List;

@Component
public class TrainerQueryService {

    private final TrainingRepository trainingRepo;
    private final TrainingMapper mapper;

    public TrainerQueryService(TrainingRepository trainingRepo, TrainingMapper mapper) {
        this.trainingRepo = trainingRepo;
        this.mapper = mapper;
    }

    public List<TrainerTrainingResponseDTO> findTrainingsByCriteria(String trainerUsername,
                                                                    LocalDate from,
                                                                    LocalDate to,
                                                                    String traineeName) {
        var spec = Specification.allOf(byTrainerUsername(trainerUsername))
                .and(fromDate(from))
                .and(toDate(to))
                .and(byTraineeUsername(traineeName));

        return trainingRepo.findAll(spec).stream()
                .map(mapper::toTrainerResponseDTO)
                .toList();
    }
}