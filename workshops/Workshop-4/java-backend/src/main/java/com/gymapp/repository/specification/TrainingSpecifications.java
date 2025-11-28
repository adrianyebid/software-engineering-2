package com.gymapp.repository.specification;

import com.gymapp.model.Training;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TrainingSpecifications {

    public static Specification<Training> byTraineeUsername(String username) {
        if (username == null || username.isBlank()) {
            return null;
        }
        return (root, query, cb) ->
                cb.equal(root.get("trainee").get("username"), username);
    }

    public static Specification<Training> fromDate(LocalDate from) {
        if (from == null) {
            return null;
        }
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("trainingDate"), from);
    }

    public static Specification<Training> toDate(LocalDate to) {
        if (to == null) {
            return null;
        }
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("trainingDate"), to);
    }

    public static Specification<Training> byTrainerUsername(String trainerName) {
        if (trainerName == null || trainerName.isBlank()) {
            return null;
        }
        return (root, query, cb) ->
                cb.equal(root.get("trainer").get("username"), trainerName);
    }

    public static Specification<Training> byTrainingType(String trainingTypeName) {
        if (trainingTypeName == null || trainingTypeName.isBlank()) {
            return null;
        }
        return (root, query, cb) ->
                cb.equal(root.get("trainingType").get("trainingTypeName"), trainingTypeName);
    }
}
