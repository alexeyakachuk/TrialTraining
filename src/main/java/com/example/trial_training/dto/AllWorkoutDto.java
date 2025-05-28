package com.example.trial_training.dto;

import com.example.trial_training.model.client.Client;
import com.example.trial_training.model.workout.Workout;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllWorkoutDto {
    private Workout workout;
    private Client client;
}
