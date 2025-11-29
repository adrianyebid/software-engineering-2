package com.gymapp.service.trainer;

import com.gymapp.dto.request.common.ChangeStatusDTO;
import com.gymapp.dto.request.trainer.*;
import com.gymapp.dto.response.trainer.*;
import com.gymapp.dto.response.training.TrainerTrainingResponseDTO;

import java.util.List;

public interface TrainerService {

    TrainerCredentialsResponseDTO register(CreateTrainerDTO dto);

    TrainerProfileResponseDTO getProfile(String username);

    TrainerResponseDTO updateProfile(UpdateTrainerDTO dto);

    void changeStatus(ChangeStatusDTO dto);

    void delete(String username);

    List<TraineeSummaryResponseDTO> getAssignedTrainees(String username);

    List<TrainerTrainingResponseDTO> getTrainingsByCriteria(TrainerTrainingFilterDTO filter);
}