package com.example.trial_training.controller.trainer;

import com.example.trial_training.controller.workout.CreateWorkoutRequest;
import com.example.trial_training.model.client.Client;
import com.example.trial_training.model.trainer.Trainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest
@AutoConfigureMockMvc
public class TrainerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private MockHttpSession session;

    @Test
    void updateTrainerTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "alexey80");
        session.setAttribute("userId", 1);

        this.mockMvc.perform(get("/trainers/1").session(session)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Алексей"))
                .andExpect(jsonPath("$.surname").value("Иванов"))
                .andExpect(jsonPath("$.birthday").value("1980-05-15"))
                .andExpect(jsonPath("$.telephone").value("+79161234567"))
                .andExpect(jsonPath("$.email").value("alexey.ivanov@example.com"))
                .andExpect(jsonPath("$.login").value("alexey80"))
                .andExpect(jsonPath("$.password").value("1111"));

        Trainer newTrainer = Trainer.builder()
                .id(1)
                .name("Алексей")
                .surname("Иванов")
                .birthday(LocalDate.of(1980, 5, 15))
                .telephone("+222222222222")
                .email("alexey.ivanov@example.com")
                .login("alexey80")
                .password("1111")
                .build();

        this.mockMvc.perform(put("/trainers").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTrainer))) // Сериализация объекта в JSON
                .andDo(print())
                .andExpect(status().isOk());
        //проверяем что данные поменялись
        this.mockMvc.perform(get("/trainers/1").session(session)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Алексей"))
                .andExpect(jsonPath("$.surname").value("Иванов"))
                .andExpect(jsonPath("$.birthday").value("1980-05-15"))
                // телефон должен поменятся
                .andExpect(jsonPath("$.telephone").value("+222222222222"))
                .andExpect(jsonPath("$.email").value("alexey.ivanov@example.com"))
                .andExpect(jsonPath("$.login").value("alexey80"))
                .andExpect(jsonPath("$.password").value("1111"));

    }

    @Test
    void updateOtherClientTest() throws Exception {
        //СОздаем сессию тренера с id 1
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "alexey80");
        session.setAttribute("userId", 1);

        // пытаемся обновить клинта с id 2
        Client newClient = Client.builder()
                .id(2)
                .name("Иван")
                .surname("Петров")
                .birthday(LocalDate.of(1990, 1, 10))
                .telephone("+11111111111")
                .email("ivan.petrov@example.com")
                .login("ivan90")
                .password("0000")
                .build();

        this.mockMvc.perform(put("/trainers").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newClient))) // Сериализация объекта в JSON
                .andDo(print())
                .andExpect(status().isForbidden()); // Ожидаем ошибку 403 Forbidden, если пользователь не имеет прав
    }

    @Test
    void findTrainer() throws Exception {
        //СОздаем сессию тренера с id 1
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "alexey80");
        session.setAttribute("userId", 1);

        this.mockMvc.perform(get("/trainers/1").session(session)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Алексей"))
                .andExpect(jsonPath("$.surname").value("Иванов"))
                .andExpect(jsonPath("$.birthday").value("1980-05-15"))
                .andExpect(jsonPath("$.telephone").value("+79161234567"))
                .andExpect(jsonPath("$.email").value("alexey.ivanov@example.com"))
                .andExpect(jsonPath("$.login").value("alexey80"))
                .andExpect(jsonPath("$.password").value("1111"));
    }

    @Test
    void findOtherTrainerTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "alexey80");
        session.setAttribute("userId", 1);
        // пытаемся получить данные другого клиента
        this.mockMvc.perform(get("/trainers/2").session(session)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void createTrainerWorkoutTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "marina85");
        session.setAttribute("userId", 2);
//        создаем новую тренировку
        CreateWorkoutRequest newWorkout = CreateWorkoutRequest.builder()
                .clientId(1)
                .trainerId(2)
                .date(LocalDate.of(2026, 10, 10))
                .startTime(LocalTime.of(11, 0, 0))
                .build();

        this.mockMvc.perform(post("/workout")
                        .contentType(MediaType.APPLICATION_JSON).session(session)
                        .content(objectMapper.writeValueAsString(newWorkout)))// Сериализация объекта в JSON
                .andDo(print())
                .andExpect(status().isOk());

        // проверяем что тренировок стало больше
        this.mockMvc.perform(get("/trainers/2/workouts").session(session)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    void deleteTrainersWorkoutTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "marina85");
        session.setAttribute("userId", 2);

        // удоляем тренировку
        this.mockMvc.perform(delete("/workout/3").session(session))
                .andExpect(status().isOk());

        // проверяем что у нас одна тренировка
        this.mockMvc.perform(get("/trainers/2/workouts").session(session)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

    }

}

// 1. Тренер пытается изменить свои данные = ок
// 2. Тренер пытается изменить чужие данные = не написан(должна вернутся ошибка)
// 3. Проверить добавление и удоление тренировки

