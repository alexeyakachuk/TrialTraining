package com.example.TrialTraining.trainer.service;

import com.example.TrialTraining.trainer.model.Trainer;
import com.example.TrialTraining.trainer.trainerDto.TrainerDto;
import com.example.TrialTraining.trainingCalendar.model.TrainingCalendar;

import java.util.List;

public interface TrainerService {

    TrainerDto create(Trainer newTrainer);

    List<TrainerDto> findAllTrainer();

    void deleteTrainer(Integer id);

    Integer updateTrainer(Trainer newTrainer);

    List<TrainingCalendar> findAllTrainerWorkouts(Integer id);

    TrainerDto findTrainer(Integer id);
}
