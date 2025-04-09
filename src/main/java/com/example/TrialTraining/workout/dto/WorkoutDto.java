package com.example.TrialTraining.workout.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkoutDto {
    @NotNull
    private String workout;
}
