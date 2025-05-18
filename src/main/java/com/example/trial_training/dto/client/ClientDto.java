package com.example.trial_training.dto.client;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class ClientDto {
    private Integer id;
    private String name;
    private String surname;
    private LocalDate birthday;
    private String telephone;
    private String email;
    private String login;
}
