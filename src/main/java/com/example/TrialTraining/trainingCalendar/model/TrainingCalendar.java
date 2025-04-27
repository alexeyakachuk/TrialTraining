package com.example.TrialTraining.trainingCalendar.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@Builder
public class TrainingCalendar {
    private String nameClient;
    private String surnameClient;
    private LocalDate date;
    private LocalTime startTime;
}
