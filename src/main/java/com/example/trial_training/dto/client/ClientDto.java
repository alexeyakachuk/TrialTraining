package com.example.trial_training.dto.client;

import com.example.trial_training.model.client.Client;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientDto {
    private Integer id;
    private String name;
    private String surname;
    private LocalDate birthday;
    private String telephone;
    private String email;
    private String login;
    private String password;

    public ClientDto(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.surname = client.getSurname();
        this.birthday = client.getBirthday();
        this.telephone = client.getTelephone();
        this.email = client.getEmail();
        this.login = client.getLogin();
        this.password = client.getPassword();
    }
}
