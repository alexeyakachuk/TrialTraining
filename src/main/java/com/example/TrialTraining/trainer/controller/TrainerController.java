package com.example.TrialTraining.trainer.controller;

import com.example.TrialTraining.trainer.model.Trainer;
import com.example.TrialTraining.trainer.service.TrainerService;
import com.example.TrialTraining.trainer.trainerDto.TrainerDto;
import com.example.TrialTraining.trainingCalendar.model.TrainingCalendar;
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

}
