package com.gymapp.service.trainingtype;

import com.gymapp.dto.response.trainingtype.TrainingTypeResponseDTO;

import java.util.List;

public interface TrainingTypeService {
    List<TrainingTypeResponseDTO> getAllTrainingTypes();
}