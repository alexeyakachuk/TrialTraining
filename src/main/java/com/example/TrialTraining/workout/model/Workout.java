package com.example.TrialTraining.workout.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Workout {
    private Integer id;
    @NotNull
    private String name;
}
