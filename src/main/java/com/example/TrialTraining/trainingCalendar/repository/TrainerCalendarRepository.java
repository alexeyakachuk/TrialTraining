package com.example.TrialTraining.trainingCalendar.repository;

import com.example.TrialTraining.trainingCalendar.model.TrainingCalendar;

import java.util.List;

public interface TrainerCalendarRepository {

    List<TrainingCalendar> findAllTrainerWorkouts(Integer id);
}
