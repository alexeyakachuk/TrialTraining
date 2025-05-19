package com.example.trial_training.dto.workout;

import com.example.trial_training.model.workout.Workout;
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

    public static WorkoutDto fromWorkout(Workout workout) {
        return WorkoutDto.builder()
                .clientId(workout.getClientId())
                .trainerId(workout.getTrainerId())
                .date(workout.getDate())
                .startTime(workout.getStartTime())
                .endTime(workout.getEndTime())
                .build();
    }
}
