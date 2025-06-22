package com.example.trial_training.dto.workout;

import com.example.trial_training.dto.client.ClientDto;
import com.example.trial_training.dto.trainer.TrainerDto;
import com.example.trial_training.model.client.Client;
import com.example.trial_training.model.trainer.Trainer;
import com.example.trial_training.model.workout.Workout;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutDto {
    private Integer id;
    private ClientDto client;
    private TrainerDto trainer;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public WorkoutDto(Workout workout, Client client, Trainer trainer) {
        this.id = workout.getId();
        this.client = ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .surname(client.getSurname())
                .birthday(client.getBirthday())
                .telephone(client.getTelephone())
                .build();
        this.trainer = TrainerDto.builder()
                .id(trainer.getId())
                .name(trainer.getName())
                .surname(trainer.getSurname())
                .birthday(trainer.getBirthday())
                .telephone(trainer.getTelephone())
                .build();
        this.date = workout.getDate();
        this.startTime = workout.getStartTime();
        this.endTime = workout.getEndTime();
    }

    public WorkoutDto(Workout workout, Client client) {
        this.id = workout.getId();
        this.client = ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .surname(client.getSurname())
                .birthday(client.getBirthday())
                .telephone(client.getTelephone())
                .build();
        this.date = workout.getDate();
        this.startTime = workout.getStartTime();
        this.endTime = workout.getEndTime();
    }

    public WorkoutDto(Workout workout, Trainer trainer) {
        this.id = workout.getId();
        this.trainer = TrainerDto.builder()
                .id(trainer.getId())
                .name(trainer.getName())
                .surname(trainer.getSurname())
                .birthday(trainer.getBirthday())
                .telephone(trainer.getTelephone())
                .build();
        this.date = workout.getDate();
        this.startTime = workout.getStartTime();
        this.endTime = workout.getEndTime();
    }
}
