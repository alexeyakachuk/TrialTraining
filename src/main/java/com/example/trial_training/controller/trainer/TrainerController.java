package com.example.trial_training.controller.trainer;

import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.exception.AuthenticationException;
import com.example.trial_training.filters.AuthFilters;
import com.example.trial_training.model.trainer.Trainer;
import com.example.trial_training.service.trainer.TrainerService;
import com.example.trial_training.dto.trainer.TrainerDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/trainers")
public class TrainerController {

    private final TrainerService trainerService;

    @Autowired
    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping
    public TrainerDto create(@Valid @RequestBody CreateTrainerRequest newTrainer) {
        return trainerService.create(newTrainer);
    }

    @GetMapping("/{id}")
    public TrainerDto findTrainer(@PathVariable Integer id) {
        return trainerService.findTrainer(id);
    }

    @GetMapping()
    public List<TrainerDto> findAllTrainers() {
        return trainerService.findAllTrainers();
    }

    @GetMapping("/{id}/workout")
    public List<WorkoutDto> findAllTrainerWorkouts(@PathVariable Integer id, HttpServletRequest request) {
//        final HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("userId") != id) {
//            throw new AuthenticationException("Не найдена сессия");
//        }
        // В следующей ветке написать ролии
        return trainerService.findAllTrainerWorkouts(id);
    }

    @PutMapping
    public Integer updateTrainer(@Valid @RequestBody  Trainer newTrainer, HttpServletRequest request) {

        return trainerService.updateTrainer(newTrainer);
    }

    @DeleteMapping
    public void deleteTrainer(HttpServletRequest request) {
        Integer sessionUserId = AuthFilters.getSessionUserId(request);
        trainerService.deleteTrainer(sessionUserId);
    }

    @GetMapping("/{id}/workouts")
    public List<WorkoutDto> findAllWorkoutsOfTrainer(@PathVariable Integer id) {
        return trainerService.findAllWorkoutsOfTrainer(id);
    }
}
