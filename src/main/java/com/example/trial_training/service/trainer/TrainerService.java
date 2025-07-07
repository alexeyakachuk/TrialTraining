package com.example.trial_training.service.trainer;

import com.example.trial_training.controller.trainer.CreateTrainerRequest;
import com.example.trial_training.dto.client.ClientDto;
import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.model.trainer.Trainer;
import com.example.trial_training.dto.trainer.TrainerDto;

import java.util.List;
import java.util.Optional;

public interface TrainerService {

    TrainerDto create(CreateTrainerRequest newTrainer);

    List<TrainerDto> findAllTrainers();

    void deleteTrainer(Integer id);

    Integer updateTrainer(Trainer newTrainer);

    TrainerDto findTrainer(Integer id);

    List<WorkoutDto> findAllWorkoutsOfTrainer(Integer id);

    List<WorkoutDto> findAllTrainerWorkouts(Integer id);

    Optional<TrainerDto> findByLogin(String login);
}
