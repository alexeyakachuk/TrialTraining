package com.example.TrialTraining.trainer.trainingCalendar;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;


@Data
@Builder
public class TrainerTrainingCalendar {
    private String nameClient;
    private String surnameClient;
    private LocalTime startTime;
}
