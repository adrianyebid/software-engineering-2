package com.gymapp.mapper;

import com.gymapp.dto.response.trainingtype.TrainingTypeResponseDTO;
import com.gymapp.model.TrainingType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingTypeMapper {

    public TrainingTypeResponseDTO toResponseDTO(TrainingType trainingType) {
        return new TrainingTypeResponseDTO(
                trainingType.getId(),
                trainingType.getTrainingTypeName()
        );
    }

    public List<TrainingTypeResponseDTO> toResponseDTOList(List<TrainingType> types) {
        return types.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}