package com.example.trial_training.workout.controller;

import com.example.trial_training.workout.dto.WorkoutDto;
import com.example.trial_training.workout.model.Workout;
import com.example.trial_training.workout.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/workout")
public class WorkoutController {

    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping
    public WorkoutDto create(@Valid @RequestBody Workout newWorkout) {
        return workoutService.create(newWorkout);
    }

    @GetMapping("/{id}")
    public WorkoutDto findWorkout(@PathVariable Integer id) {
        return workoutService.findWorkout(id);
    }

    @GetMapping
    public List<WorkoutDto> findAllWorkout() {
        return workoutService.findAllWorkout();
    }

    @DeleteMapping("{id}")
    public void deleteWorkout(@PathVariable Integer id) {
        workoutService.deleteWorkout(id);
    }
}
