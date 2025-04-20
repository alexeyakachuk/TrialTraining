package com.example.TrialTraining.workout.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Workout {
    private Integer id;
    private Integer clientId;
    private Integer trainerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
