package com.example.trial_training.service.workout;

import com.example.trial_training.controller.workout.CreateWorkoutRequest;
import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.model.workout.Workout;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface WorkoutService {
    WorkoutDto create(CreateWorkoutRequest newWorkout);

    List<WorkoutDto> findAllWorkouts();

    WorkoutDto findWorkout(Integer id);

    void deleteWorkout(Integer id);
}
