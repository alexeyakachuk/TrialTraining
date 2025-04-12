package com.example.TrialTraining.trainer.trainerDto;


import com.example.TrialTraining.workout.dto.WorkoutDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class TrainerDto {
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
//    private Set<WorkoutDto> workout;
}
