package com.example.TrialTraining.workout.service;

import com.example.TrialTraining.workout.dto.WorkoutDto;
import com.example.TrialTraining.workout.model.Workout;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkoutService {
    WorkoutDto create(Workout workout);

    List<WorkoutDto> findAllWorkout();

    WorkoutDto findWorkout(Integer id);

    void deleteWorkout(Integer id);
}
