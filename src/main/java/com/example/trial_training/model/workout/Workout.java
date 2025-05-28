package com.example.trial_training.model.workout;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Workout {
    private Integer id;
    private Integer clientId;
    private Integer trainerId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
