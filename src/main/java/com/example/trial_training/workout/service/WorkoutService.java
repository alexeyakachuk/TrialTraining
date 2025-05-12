package com.example.trial_training.workout.service;

import com.example.trial_training.workout.dto.WorkoutDto;
import com.example.trial_training.workout.model.Workout;

import java.util.List;

public interface WorkoutService {
    WorkoutDto create(Workout workout);

    List<WorkoutDto> findAllWorkout();

    WorkoutDto findWorkout(Integer id);

    void deleteWorkout(Integer id);
}
