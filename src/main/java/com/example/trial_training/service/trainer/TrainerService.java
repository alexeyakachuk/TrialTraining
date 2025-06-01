package com.example.trial_training.service.trainer;

import com.example.trial_training.controller.trainer.CreateTrainerRequest;
import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.model.trainer.Trainer;
import com.example.trial_training.dto.trainer.TrainerDto;

import java.util.List;

public interface TrainerService {

    TrainerDto create(CreateTrainerRequest newTrainer);

    List<TrainerDto> findAllTrainers();

    void deleteTrainer(Integer id);

    Integer updateTrainer(Trainer newTrainer);

    TrainerDto findTrainer(Integer id);

    List<WorkoutDto> findAllWorkoutsOfTrainer(Integer id);
}
