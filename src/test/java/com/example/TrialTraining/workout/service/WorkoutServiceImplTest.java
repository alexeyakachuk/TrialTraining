package com.example.TrialTraining.workout.service;

import com.example.TrialTraining.client.model.Client;
import com.example.TrialTraining.client.repository.ClientRepository;
import com.example.TrialTraining.trainer.model.Trainer;
import com.example.TrialTraining.trainer.repository.TrainerRepository;
import com.example.TrialTraining.workout.dto.WorkoutDto;
import com.example.TrialTraining.workout.model.Workout;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WorkoutServiceImplTest {
    private final ClientRepository clientRepository;
    private final TrainerRepository trainerRepository;
    private final WorkoutService workoutService;
    private final NamedParameterJdbcOperations jdbcOperations;
    private Trainer testTrainer;
    private Client testClient1;
    private Client testClient2;
    private Client testClient3;
    private Workout testWorkout1;
    private Workout testWorkout2;
    private Workout testWorkout3;



    @BeforeEach
    void setUp() {
        jdbcOperations.update("DELETE FROM trainer", Map.of());
        jdbcOperations.update("DELETE FROM client", Map.of());
        jdbcOperations.update("DELETE FROM workout", Map.of());

        testTrainer = trainerRepository.create(Trainer.builder()
                .name("trainer")
                .surname("surname")
                .birthday(LocalDate.of(1989, 10, 17))
                .telephone("0000")
                .email("0@.ru")
                .login("1111")
                .build());

        testClient1 = clientRepository.create(Client.builder()
                .name("client1")
                .surname("surname1")
                .birthday(LocalDate.of(1990, 11, 18))
                .telephone("1111")
                .email("1@.ru")
                .login("1111")
                .build());

        testClient2 = clientRepository.create(Client.builder()
                .name("client2")
                .surname("surname2")
                .birthday(LocalDate.of(1991, 12, 19))
                .telephone("2222")
                .email("2@.ru")
                .login("2222")
                .build());

        testClient3 = clientRepository.create(Client.builder()
                .name("client3")
                .surname("surname3")
                .birthday(LocalDate.of(1993, 11, 19))
                .telephone("3333")
                .email("3@.ru")
                .login("3333")
                .build());

        testWorkout1 = Workout.builder()
                .clientId(testClient1.getId())
                .trainerId(testTrainer.getId())
                .date(LocalDate.of(2024, 4, 23))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .build();

        testWorkout2 = Workout.builder()
                .clientId(testClient2.getId())
                .trainerId(testTrainer.getId())
                .date(LocalDate.of(2024, 4, 23))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .build();

        testWorkout3 = Workout.builder()
                .clientId(testClient3.getId())
                .trainerId(testTrainer.getId())
                .date(LocalDate.of(2024, 4, 23))
                .startTime(LocalTime.of(12, 0))
                .endTime(LocalTime.of(13, 0))
                .build();
    }

    @Test
    void createTest() throws InterruptedException {

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                workoutService.create(testWorkout1);
            }

        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                workoutService.create(testWorkout2);
            }
        });

        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                workoutService.create(testWorkout3);
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();


        List<WorkoutDto> allWorkout = workoutService.findAllWorkout();
        System.out.println("количество тренировок - " + allWorkout.size());


        Assertions.assertEquals(2, allWorkout.size());


    }
}
