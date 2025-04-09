package com.example.TrialTraining.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@Builder
public class Client {
    @NonNull
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    @PastOrPresent(message = "не может быть в будущем")
    private LocalDate birthday;
    @NonNull
    private String telephone;
    @NonNull
    @Email
    private String email;
    @NotBlank(message = "Не может быть пустым")
    @NotNull
    private String login;

}
