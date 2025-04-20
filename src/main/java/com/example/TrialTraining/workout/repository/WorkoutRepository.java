package com.example.TrialTraining.workout.repository;

import com.example.TrialTraining.workout.model.Workout;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkoutRepository {

    Workout create(Workout workout);

    Workout findWorkout(Integer id);

    List<Workout> findAllWorkout();
}
