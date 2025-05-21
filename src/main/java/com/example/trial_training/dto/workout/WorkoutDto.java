package com.example.trial_training.dto.workout;

import com.example.trial_training.model.client.Client;
import com.example.trial_training.model.trainer.Trainer;
import com.example.trial_training.model.workout.Workout;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class WorkoutDto {
    private Integer id;
    private Client client;
    private Trainer trainer;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public static WorkoutDto fromWorkout(Workout workout, Client client, Trainer trainer) {
        return WorkoutDto.builder()
                .id(workout.getId())
                .client(Client.builder()
                        .name(client.getName())
                        .surname(client.getSurname())
                        .birthday(client.getBirthday())
                        .telephone(client.getTelephone())
                        .build())
                .trainer(Trainer.builder()
                        .name(trainer.getName())
                        .surname(trainer.getSurname())
                        .birthday(trainer.getBirthday())
                        .telephone(trainer.getTelephone())
                        .build())
                .date(workout.getDate())
                .startTime(workout.getStartTime())
                .endTime(workout.getEndTime())
                .build();
    }
}
