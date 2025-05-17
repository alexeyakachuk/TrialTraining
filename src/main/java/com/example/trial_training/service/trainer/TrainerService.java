package com.example.trial_training.service.trainer;

import com.example.trial_training.CreateTrainerRequest;
import com.example.trial_training.model.trainer.Trainer;
import com.example.trial_training.dto.trainer.TrainerDto;
import com.example.trial_training.trainingCalendar.model.TrainingCalendar;

import java.util.List;

public interface TrainerService {

    TrainerDto create(CreateTrainerRequest newTrainer);

    List<TrainerDto> findAllTrainer();

    void deleteTrainer(Integer id);

    Integer updateTrainer(Trainer newTrainer);

    List<TrainingCalendar> findAllTrainerWorkouts(Integer id);

    TrainerDto findTrainer(Integer id);
}
