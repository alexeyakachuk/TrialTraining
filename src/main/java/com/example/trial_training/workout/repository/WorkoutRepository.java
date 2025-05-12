package com.example.trial_training.workout.repository;

import com.example.trial_training.workout.model.Workout;

import java.util.List;

public interface WorkoutRepository {

    Workout create(Workout workout);

    Workout findWorkout(Integer id);

    List<Workout> findAllWorkout();

    void deleteWorkout(Integer id);
}
