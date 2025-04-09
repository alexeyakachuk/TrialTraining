package com.example.TrialTraining.trainer.model;

import com.example.TrialTraining.workout.dto.WorkoutDto;
import com.example.TrialTraining.workout.model.Workout;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Trainer {
    @NotNull
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @NotNull
    @PastOrPresent(message = "не может быть в будущем")
    private LocalDate birthday;
    @NotNull
    private String telephone;
    @NotNull
    @Email
    private String email;
    @NotBlank(message = "Не может быть пустым")
    @NotNull
    private String login;
    private Set<WorkoutDto> workout;


}
