package com.example.trial_training.repository.trainer;

import com.example.trial_training.CreateTrainerRequest;
import com.example.trial_training.model.trainer.Trainer;

import java.util.List;

public interface TrainerRepository {

    Trainer create(CreateTrainerRequest newTrainer);

    List<Trainer> findAllTrainers();

    Trainer findTrainer(Integer id);

    Integer updateTrainer(Trainer newTrainer);

    void deleteTrainer(Integer id);
}
