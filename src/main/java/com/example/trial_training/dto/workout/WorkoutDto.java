package com.example.trial_training.dto.workout;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class WorkoutDto {
    private Integer clientId;
    private Integer trainerId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
