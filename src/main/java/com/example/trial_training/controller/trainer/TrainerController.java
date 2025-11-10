package com.example.trial_training.controller.trainer;

import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.exception.AuthenticationException;
import com.example.trial_training.filters.AuthFilters;
import com.example.trial_training.model.trainer.Trainer;
import com.example.trial_training.service.trainer.TrainerService;
import com.example.trial_training.dto.trainer.TrainerDto;
import com.example.trial_training.service.trainer.TrainerServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/trainers")
public class TrainerController {

    private final TrainerServiceImpl trainerServiceImpl;

    @Autowired
    public TrainerController(TrainerServiceImpl trainerServiceImpl) {
        this.trainerServiceImpl = trainerServiceImpl;
    }

    @PostMapping
    public TrainerDto create(@Valid @RequestBody Trainer newTrainer) {   //CreateTrainerRequest newTrainer
        return trainerServiceImpl.create(newTrainer);
    }

    @GetMapping("/{id}")
    public TrainerDto findTrainer(@PathVariable Integer id) {
        return trainerServiceImpl.findTrainer(id);
    }

    @GetMapping()
    public List<TrainerDto> findAllTrainers() {
        return trainerServiceImpl.findAllTrainers();
    }


    @PutMapping
    //Integer
    public Trainer updateTrainer(@Valid @RequestBody  Trainer newTrainer) { //HttpServletRequest request

        return trainerServiceImpl.updateTrainer(newTrainer);
    }

    @DeleteMapping("/{id}")
    public void deleteTrainer(@PathVariable Integer id) {  // HttpServletRequest request
//        Integer sessionUserId = AuthFilters.getSessionUserId(request);
//        trainerServiceImpl.deleteTrainer(sessionUserId);
        trainerServiceImpl.deleteTrainer(id);
    }

    @GetMapping("/{id}/workouts")
    public List<WorkoutDto> findAllWorkoutsOfTrainer(@PathVariable Integer id) {
        return trainerServiceImpl.findAllWorkoutsOfTrainer(id);
    }
}
