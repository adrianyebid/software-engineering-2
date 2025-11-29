package com.gymapp.service.trainingtype;

import com.gymapp.dto.response.trainingtype.TrainingTypeResponseDTO;
import com.gymapp.mapper.TrainingTypeMapper;
import com.gymapp.model.TrainingType;
import com.gymapp.repository.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingTypeServiceImplTest {

    private TrainingTypeRepository trainingTypeRepo;
    private TrainingTypeMapper trainingTypeMapper;
    private TrainingTypeServiceImpl service;

    @BeforeEach
    void setUp() {
        trainingTypeRepo = mock(TrainingTypeRepository.class);
        trainingTypeMapper = mock(TrainingTypeMapper.class);
        service = new TrainingTypeServiceImpl(trainingTypeRepo, trainingTypeMapper);
    }

    @Test
    void shouldReturnAllTrainingTypes() {
        TrainingType type1 = TrainingType.builder().id(1L).trainingTypeName("Yoga").build();
        TrainingType type2 = TrainingType.builder().id(2L).trainingTypeName("Pilates").build();
        List<TrainingType> entities = List.of(type1, type2);

        TrainingTypeResponseDTO dto1 = new TrainingTypeResponseDTO(1L, "Yoga");
        TrainingTypeResponseDTO dto2 = new TrainingTypeResponseDTO(2L, "Pilates");
        List<TrainingTypeResponseDTO> expected = List.of(dto1, dto2);

        when(trainingTypeRepo.findAll()).thenReturn(entities);
        when(trainingTypeMapper.toResponseDTOList(entities)).thenReturn(expected);

        List<TrainingTypeResponseDTO> result = service.getAllTrainingTypes();

        assertEquals(2, result.size());
        assertEquals("Yoga", result.get(0).name());
        assertEquals("Pilates", result.get(1).name());
        verify(trainingTypeRepo).findAll();
        verify(trainingTypeMapper).toResponseDTOList(entities);
    }
}