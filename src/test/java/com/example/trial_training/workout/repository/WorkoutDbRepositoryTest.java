package com.example.trial_training.workout.repository;

import com.example.trial_training.CreateClientRequest;
import com.example.trial_training.CreateTrainerRequest;
import com.example.trial_training.model.client.Client;
import com.example.trial_training.repository.client.ClientRepository;
import com.example.trial_training.model.trainer.Trainer;
import com.example.trial_training.repository.trainer.TrainerRepository;
import com.example.trial_training.model.workout.Workout;
import com.example.trial_training.repository.workout.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WorkoutDbRepositoryTest {
    private final NamedParameterJdbcOperations jdbcOperations;
    private final WorkoutRepository workoutRepository;
    private final TrainerRepository trainerRepository;
    private final ClientRepository clientRepository;
    private Client testClient1;
    private Client testClient2;
    private Trainer testTrainer1;
    private Trainer testTrainer2;
    private Workout testWorkout1;
    private Workout testWorkout2;


    @BeforeEach
    void setUp() {
        jdbcOperations.update("DELETE FROM trainer", Map.of());
        jdbcOperations.update("DELETE FROM client", Map.of());
        jdbcOperations.update("DELETE FROM workout", Map.of());

        testClient1 = clientRepository.create(CreateClientRequest.builder()
                .name("client1")
                .surname("surname1")
                .birthday(LocalDate.of(1991, 10, 10))
                .telephone("1111")
                .email("1@y.ru")
                .login("1111")
                .build());


        testClient2 = clientRepository.create(CreateClientRequest.builder()
                .name("client2")
                .surname("surname2")
                .birthday(LocalDate.of(1992, 10, 10))
                .telephone("2222")
                .email("2@y.ru")
                .login("2222")
                .build());


        testTrainer1 = trainerRepository.create(CreateTrainerRequest.builder()
                .name("trainer1")
                .surname("surname1")
                .birthday(LocalDate.of(1991, 10, 10))
                .telephone("1111")
                .email("1@y.ru")
                .login("1111")
                .build());


        testTrainer2 = trainerRepository.create(CreateTrainerRequest.builder()
                .name("trainer2")
                .surname("surname2")
                .birthday(LocalDate.of(1992, 10, 10))
                .telephone("2222")
                .email("2@y.ru")
                .login("2222")
                .build());


        testWorkout1 = workoutRepository.create(Workout.builder()
                .clientId(testClient1.getId())
                .trainerId(testTrainer1.getId())
                .date(LocalDate.of(2024, 4, 23))
                .startTime(LocalTime.of(10, 0))
                .build());

        testWorkout2 = workoutRepository.create(Workout.builder()
                .clientId(testClient2.getId())
                .trainerId(testTrainer1.getId())
                .date(LocalDate.of(2024, 4, 23))
                .startTime(LocalTime.of(11, 0))
                .build());
    }

    @Test
    void createTest() {
        Workout newWorkout = Workout.builder()
                .clientId(testClient1.getId())
                .trainerId(testTrainer1.getId())
                .date(LocalDate.of(2025, 5, 5))
                .startTime(LocalTime.of(10, 0))
                .build();

        Workout workout = workoutRepository.create(newWorkout);

        assertNotNull(workout.getId());
        assertEquals(newWorkout.getClientId(), workout.getClientId(), "id клиентов должны совподать");
        assertEquals(newWorkout.getTrainerId(), workout.getTrainerId(), "id тренеров должны совподать");
        assertEquals(newWorkout.getDate(), workout.getDate(), "дата должна совподать");
        assertEquals(newWorkout.getStartTime(), workout.getStartTime(), "начало тренировки должно совподать");
        assertEquals(LocalTime.of(11, 0), workout.getEndTime(), "конец тренировки должен совподать");
    }

    @Test
    void findWorkoutTest() {
        Workout workout = workoutRepository.findWorkout(testWorkout1.getId());

        assertNotNull(workout);
        System.out.println(workout);
        assertEquals(testWorkout1.getClientId(), workout.getClientId(), "id клиентов должны совподать");
        assertEquals(testWorkout1.getTrainerId(), workout.getTrainerId(), "id тренеров должны совподать");
        assertEquals(testWorkout1.getDate(), workout.getDate(), "дата должна совподать");
        assertEquals(testWorkout1.getStartTime(), workout.getStartTime(), "начало тренировки должно совподать");
        assertEquals(LocalTime.of(11, 0), workout.getEndTime(), "конец тренировки должен совподать");
    }

    @Test
    void findAllWorkouts() {
        List<Workout> allWorkout = workoutRepository.findAllWorkouts();

        assertEquals(2, allWorkout.size());
    }

    @Test
    void deleteWorkout() {
        workoutRepository.deleteWorkout(testWorkout1.getId());
        List<Workout> allWorkout = workoutRepository.findAllWorkouts();

        assertEquals(1, allWorkout.size());
    }
}
