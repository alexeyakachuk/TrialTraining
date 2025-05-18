package com.example.trial_training.dto.trainer;

import com.example.trial_training.model.trainer.Trainer;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class TrainerDto {
    private Integer id;
    private String name;
    private String surname;
    private LocalDate birthday;
    private String telephone;
    private String email;
    private String login;

    public static TrainerDto fromTrainer(Trainer trainer) {
        return TrainerDto.builder()
                .id(trainer.getId())
                .name(trainer.getName())
                .surname(trainer.getSurname())
                .birthday(trainer.getBirthday())
                .telephone(trainer.getTelephone())
                .email(trainer.getEmail())
                .login(trainer.getLogin())
                .build();
    }
}
