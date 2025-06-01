package com.example.trial_training.dto.trainer;

import com.example.trial_training.model.trainer.Trainer;
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
public class TrainerDto {
    private Integer id;
    private String name;
    private String surname;
    private LocalDate birthday;
    private String telephone;
    private String email;
    private String login;

    public TrainerDto(Trainer trainer) {
        this.id = trainer.getId();
        this.name = trainer.getName();
        this.surname = trainer.getSurname();
        this.birthday = trainer.getBirthday();
        this.telephone = trainer.getTelephone();
        this.email = trainer.getEmail();
        this.login = trainer.getLogin();
}
}
