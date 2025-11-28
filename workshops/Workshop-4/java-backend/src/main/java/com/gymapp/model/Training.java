package com.gymapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "trainings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Training {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_name",nullable = false)
    private String trainingName;

    @Column(name = "training_date",nullable = false)
    private LocalDate trainingDate;

    @Column(name = "training_duration",nullable = false)
    private int trainingDuration; // minutos

    @ManyToOne
    @JoinColumn(name = "trainee_id", nullable = false)
    @ToString.Exclude
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    @ToString.Exclude
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name = "training_type_id", nullable = false)
    @ToString.Exclude
    private TrainingType trainingType;
}
