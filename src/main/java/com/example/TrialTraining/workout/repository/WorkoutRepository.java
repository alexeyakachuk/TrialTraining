package com.example.TrialTraining.workout.repository;

import com.example.TrialTraining.workout.model.Workout;

import java.time.LocalDateTime;

public interface WorkoutRepository {

    Workout create(Integer clientId, Integer trainerId, LocalDateTime dateTime);

    Workout findWorkout(Integer id);
}
