package com.example.TrialTraining.trainer.repository;

import com.example.TrialTraining.trainer.model.Trainer;

import java.util.List;

public interface TrainerRepository {

    Trainer create(Trainer newTrainer);

    List<Trainer> findAllTrainer();

    Trainer findTrainer(Integer id);

    Trainer checkEmail(String email);
}
