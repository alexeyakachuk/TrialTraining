package com.example.trial_training.repository.workout;

import com.example.trial_training.controller.workout.CreateWorkoutRequest;
import com.example.trial_training.model.workout.Workout;

import java.util.List;

public interface WorkoutRepository {

    Workout create(CreateWorkoutRequest workout);

    Workout findWorkout(Integer id);

    List<Workout> findAllWorkouts();

    void deleteWorkout(Integer id);
}
