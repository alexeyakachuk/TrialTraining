package com.example.TrialTraining.trainer.service;

import com.example.TrialTraining.trainer.model.Trainer;
import com.example.TrialTraining.trainer.trainerDto.TrainerDto;

import java.util.List;

public interface TrainerService {
    TrainerDto create(Trainer newTrainer);

    List<TrainerDto> findAllTrainer();

    TrainerDto findTrainer(Integer id);
}
