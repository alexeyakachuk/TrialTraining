package com.example.trial_training.controller.workout;

import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.model.workout.Workout;
import com.example.trial_training.service.workout.WorkoutService;
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
