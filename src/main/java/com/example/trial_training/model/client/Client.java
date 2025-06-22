package com.example.trial_training.model.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Client {
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
    @NotNull
    private String password;
}
