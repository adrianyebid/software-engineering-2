package com.gymapp.service.trainee;

import com.gymapp.dto.request.common.ChangeStatusDTO;
import com.gymapp.dto.request.trainee.*;
import com.gymapp.dto.response.trainee.TraineeCredentialsResponseDTO;
import com.gymapp.dto.response.trainee.TraineeProfileResponseDTO;
import com.gymapp.dto.response.trainee.TraineeResponseDTO;
import com.gymapp.dto.response.trainee.TrainerSummaryResponseDTO;
import com.gymapp.dto.response.training.TraineeTrainingResponseDTO;
import java.util.List;

public interface TraineeService {

    TraineeCredentialsResponseDTO register(CreateTraineeDTO dto);

    TraineeProfileResponseDTO getProfile(String username);

    TraineeResponseDTO updateProfile(UpdateTraineeDTO dto);

    void changeStatus(ChangeStatusDTO dto);

    void delete(String username);

    List<TrainerSummaryResponseDTO> assignTrainers(AssignTrainersDTO dto);

    List<TrainerSummaryResponseDTO> getActiveUnassignedTrainers(String username);

    List<TraineeTrainingResponseDTO> getTrainingsByCriteria(TraineeTrainingFilterDTO filter);
}