package com.gymapp.repository.specification;

import com.gymapp.model.Training;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TrainingSpecifications {

    public static Specification<Training> byTraineeUsername(String username) {
        return (root, query, cb) -> cb.equal(root.get("trainee").get("username"), username);
    }

    public static Specification<Training> fromDate(LocalDate from) {
        return (root, query, cb) -> from != null ? cb.greaterThanOrEqualTo(root.get("trainingDate"), from) : null;
    }

    public static Specification<Training> toDate(LocalDate to) {
        return (root, query, cb) -> to != null ? cb.lessThanOrEqualTo(root.get("trainingDate"), to) : null;
    }

    public static Specification<Training> byTrainerUsername(String trainerName) {
        return (root, query, cb) -> trainerName != null ? cb.equal(root.get("trainer").get("username"), trainerName) : null;
    }

    public static Specification<Training> byTrainingType(String trainingTypeName) {
        return (root, query, cb) -> trainingTypeName != null ? cb.equal(root.get("trainingType").get("trainingTypeName"), trainingTypeName) : null;
    }
}
