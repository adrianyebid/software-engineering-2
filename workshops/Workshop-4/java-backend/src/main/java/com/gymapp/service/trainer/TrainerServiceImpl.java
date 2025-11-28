package com.gymapp.service.trainer;

import com.gymapp.dto.request.common.ChangeStatusDTO;
import com.gymapp.dto.request.trainer.CreateTrainerDTO;
import com.gymapp.dto.request.trainer.TrainerTrainingFilterDTO;
import com.gymapp.dto.request.trainer.UpdateTrainerDTO;
import com.gymapp.dto.response.trainer.TraineeSummaryResponseDTO;
import com.gymapp.dto.response.trainer.TrainerCredentialsResponseDTO;
import com.gymapp.dto.response.trainer.TrainerProfileResponseDTO;
import com.gymapp.dto.response.trainer.TrainerResponseDTO;
import com.gymapp.dto.response.training.TrainerTrainingResponseDTO;
import com.gymapp.mapper.TrainerMapper;
import com.gymapp.model.*;
import com.gymapp.repository.TrainerRepository;
import com.gymapp.service.trainer.factory.TrainerFactory;
import com.gymapp.service.trainer.query.TrainerQueryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class TrainerServiceImpl implements TrainerService {

    private final TrainerFactory trainerFactory;
    private final TrainerMapper trainerMapper;
    private final TrainerQueryService queryService;
    private final TrainerRepository trainerRepository;

    public TrainerServiceImpl(TrainerFactory trainerFactory,
                              TrainerMapper trainerMapper,
                              TrainerQueryService queryService,
                              TrainerRepository trainerRepository) {
        this.trainerFactory = trainerFactory;
        this.trainerMapper = trainerMapper;
        this.queryService = queryService;
        this.trainerRepository = trainerRepository;
    }

    @Override
    public TrainerCredentialsResponseDTO register(CreateTrainerDTO dto) {
        return trainerFactory.create(dto);
    }

    @Override
    @Transactional(readOnly = true)
    public TrainerProfileResponseDTO getProfile(String username) {
        Trainer trainer = findTrainerByUsername(username);
        return trainerMapper.toProfileDTO(trainer);
    }

    @Override
    public TrainerResponseDTO updateProfile(UpdateTrainerDTO dto) {
        Trainer trainer = findTrainerByUsername(dto.username());
        trainerFactory.update(trainer, dto);
        return trainerMapper.toResponseDTO(trainer);
    }

    @Override
    public void changeStatus(ChangeStatusDTO dto) {
        Trainer trainer = findTrainerByUsername(dto.username());

        if (dto.isActive()) trainer.activate();
        else trainer.deactivate();

        trainerRepository.save(trainer);
    }

    @Override
    public void delete(String username) {
        Trainer trainer = findTrainerByUsername(username);
        trainerFactory.delete(trainer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TraineeSummaryResponseDTO> getAssignedTrainees(String username) {
        Trainer trainer = findTrainerByUsername(username);
        return trainerMapper.toTraineeSummaryList(trainer.getTrainees().stream().toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainerTrainingResponseDTO> getTrainingsByCriteria(TrainerTrainingFilterDTO filter) {
        return queryService.findTrainingsByCriteria(
                filter.getUsername(),
                filter.getPeriodFrom(),
                filter.getPeriodTo(),
                filter.getTraineeUsername()
        );
    }

    private Trainer findTrainerByUsername(String username) {
        return trainerRepository.findByUsername(username)
                .orElseThrow(() ->
                    new IllegalArgumentException("Trainer not found with username: " + username)
                );
    }
}


