package com.example.TrialTraining;

import com.example.TrialTraining.myEnum.Workout;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Objects;

public class Trainer {
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
    private Enum<Workout> workout;

    public Trainer(Integer id, String name, LocalDate birthday, String surname, String telephone, String email, String login, Enum<Workout> workout) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.surname = surname;
        this.telephone = telephone;
        this.email = email;
        this.login = login;
        this.workout = workout;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Enum<Workout> getWorkout() {
        return workout;
    }

    public void setWorkout(Enum<Workout> workout) {
        this.workout = workout;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Trainer trainer = (Trainer) o;
        return Objects.equals(getId(), trainer.getId()) && Objects.equals(getName(), trainer.getName()) && Objects.equals(getSurname(), trainer.getSurname()) && Objects.equals(getBirthday(), trainer.getBirthday()) && Objects.equals(getTelephone(), trainer.getTelephone()) && Objects.equals(getEmail(), trainer.getEmail()) && Objects.equals(getLogin(), trainer.getLogin()) && Objects.equals(getWorkout(), trainer.getWorkout());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getSurname(), getBirthday(), getTelephone(), getEmail(), getLogin(), getWorkout());
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthday=" + birthday +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", workout=" + workout +
                '}';
    }
}
