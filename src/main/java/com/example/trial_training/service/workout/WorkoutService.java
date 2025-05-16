package com.example.trial_training.service.workout;

import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.model.workout.Workout;

import java.util.List;

public interface WorkoutService {
    WorkoutDto create(Workout workout);

    List<WorkoutDto> findAllWorkout();

    WorkoutDto findWorkout(Integer id);

    void deleteWorkout(Integer id);
}
