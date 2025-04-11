package com.example.TrialTraining.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


import java.time.LocalDate;

@Data
@Builder

public class ClientDto {

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
}
