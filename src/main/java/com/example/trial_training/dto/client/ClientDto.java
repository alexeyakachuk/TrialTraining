package com.example.trial_training.dto.client;

import com.example.trial_training.model.client.Client;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

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

    public static ClientDto fromClient(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .surname(client.getSurname())
                .birthday(client.getBirthday())
                .telephone(client.getTelephone())
                .email(client.getEmail())
                .login(client.getLogin())
                .build();
    }

//    public ClientDto(Client client) {
//        this.id = client.getId();
//        this.name = client.getName();
//        this.surname = client.getSurname();
//        this.birthday = client.getBirthday();
//        this.telephone = client.getTelephone();
//        this.email = client.getEmail();
//        this.login = client.getLogin();
//    }
}
