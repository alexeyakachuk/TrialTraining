package com.example.trial_training.trainer.service;

import com.example.trial_training.trainer.model.Trainer;
import com.example.trial_training.trainer.trainerDto.TrainerDto;
import com.example.trial_training.trainingCalendar.model.TrainingCalendar;

import java.util.List;

public interface TrainerService {

    TrainerDto create(Trainer newTrainer);

    List<TrainerDto> findAllTrainer();

    void deleteTrainer(Integer id);

    Integer updateTrainer(Trainer newTrainer);

    List<TrainingCalendar> findAllTrainerWorkouts(Integer id);

    TrainerDto findTrainer(Integer id);
}
