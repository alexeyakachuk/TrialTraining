package com.example.trial_training.controller.trainer;

import com.example.trial_training.model.trainer.Trainer;
import com.example.trial_training.service.trainer.TrainerService;
import com.example.trial_training.dto.trainer.TrainerDto;
import com.example.trial_training.trainingCalendar.model.TrainingCalendar;
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
    public TrainerDto create(@Valid @RequestBody Trainer newTrainer) {
        return trainerService.create(newTrainer);
    }

    @GetMapping("/{id}")
    public TrainerDto findTrainer(@PathVariable Integer id) {
        return trainerService.findTrainer(id);
    }

    @GetMapping()
    public List<TrainerDto> findAllTrainer() {
        return trainerService.findAllTrainer();
    }

    @GetMapping("/{id}/workout")
    public List<TrainingCalendar> findAllTrainerWorkouts(@PathVariable Integer id) {
        return trainerService.findAllTrainerWorkouts(id);
    }

    @PutMapping
    public Integer updateTrainer(@Valid @RequestBody  Trainer newTrainer) {
        return trainerService.updateTrainer(newTrainer);
    }

}
