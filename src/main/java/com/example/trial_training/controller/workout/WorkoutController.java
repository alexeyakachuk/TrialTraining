package com.example.trial_training.controller.workout;

import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.exception.AuthenticationException;
import com.example.trial_training.filters.AuthFilters;
import com.example.trial_training.model.workout.Workout;
import com.example.trial_training.service.workout.WorkoutService;
import jakarta.servlet.http.HttpServletRequest;
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
    public WorkoutDto create(@Valid @RequestBody CreateWorkoutRequest newWorkout) {
        return workoutService.create(newWorkout);
    }

    @GetMapping("/{id}")
    public WorkoutDto findWorkout(@PathVariable Integer id, HttpServletRequest request) {
        WorkoutDto workout = workoutService.findWorkout(id);
//        Integer sessionUserId = AuthFilters.getSessionUserId(request);
//        if (!workout.getClient().getId().equals(sessionUserId)) {
//            throw new AuthenticationException("Доступ запрещен. Пройдите аунтефикацию");
//        }
        return workout;
    }

    @GetMapping
    public List<WorkoutDto> findAllWorkouts() {
        return workoutService.findAllWorkouts();
    }

    @DeleteMapping("{id}")
    public void deleteWorkout(@PathVariable Integer id) {
        workoutService.deleteWorkout(id);
    }
}
