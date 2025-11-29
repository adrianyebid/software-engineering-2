package com.gymapp.mapper;

import com.gymapp.dto.response.trainingtype.TrainingTypeResponseDTO;
import com.gymapp.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrainingTypeMapperTest {

    private TrainingTypeMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TrainingTypeMapper();
    }

    @Test
    void shouldMapTrainingTypeToResponseDTO() {
        TrainingType type = TrainingType.builder()
                .id(1L)
                .trainingTypeName("Yoga")
                .build();

        TrainingTypeResponseDTO dto = mapper.toResponseDTO(type);

        assertEquals(1L, dto.id());
        assertEquals("Yoga", dto.name());
    }

    @Test
    void shouldMapTrainingTypeListToResponseDTOList() {
        TrainingType type1 = TrainingType.builder().id(1L).trainingTypeName("Yoga").build();
        TrainingType type2 = TrainingType.builder().id(2L).trainingTypeName("Pilates").build();

        List<TrainingTypeResponseDTO> result = mapper.toResponseDTOList(List.of(type1, type2));

        assertEquals(2, result.size());
        assertEquals("Yoga", result.get(0).name());
        assertEquals("Pilates", result.get(1).name());
    }
}
