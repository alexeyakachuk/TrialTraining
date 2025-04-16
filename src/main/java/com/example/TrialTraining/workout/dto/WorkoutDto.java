package com.example.TrialTraining.workout.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WorkoutDto {
    private Integer clientId;
    private Integer trainerId;
    private LocalDateTime dateTime;
}
