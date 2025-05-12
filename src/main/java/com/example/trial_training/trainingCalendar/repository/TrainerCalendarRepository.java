package com.example.trial_training.trainingCalendar.repository;

import com.example.trial_training.trainingCalendar.model.TrainingCalendar;

import java.util.List;

public interface TrainerCalendarRepository {

    List<TrainingCalendar> findAllTrainerWorkouts(Integer id);
}
