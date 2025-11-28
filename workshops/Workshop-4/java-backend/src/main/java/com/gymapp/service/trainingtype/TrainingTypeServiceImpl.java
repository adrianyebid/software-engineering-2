package com.gymapp.service.trainingtype;

import com.gymapp.dto.response.trainingtype.TrainingTypeResponseDTO;
import com.gymapp.mapper.TrainingTypeMapper;
import com.gymapp.repository.TrainingTypeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepo;
    private final TrainingTypeMapper trainingTypeMapper;

    public TrainingTypeServiceImpl(TrainingTypeRepository trainingTypeRepo,
                                   TrainingTypeMapper trainingTypeMapper) {
        this.trainingTypeRepo = trainingTypeRepo;
        this.trainingTypeMapper = trainingTypeMapper;
    }

    @Override
    public List<TrainingTypeResponseDTO> getAllTrainingTypes() {
        return trainingTypeMapper.toResponseDTOList(trainingTypeRepo.findAll());
    }
}

