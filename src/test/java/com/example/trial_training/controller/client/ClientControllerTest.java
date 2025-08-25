package com.example.trial_training.controller.client;

import com.example.trial_training.controller.workout.CreateWorkoutRequest;
import com.example.trial_training.model.client.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

public class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private MockHttpSession session;

    @Test
    void updateClientTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "ivan90");
        session.setAttribute("userId", 1);

        Client newClient = Client.builder()
                .id(1)
                .name("Иван")
                .surname("Петров")
                .birthday(LocalDate.of(1990, 1, 10))
                .telephone("+11111111111")
                .email("ivan.petrov@example.com")
                .login("ivan90")
                .password("0000")
                .build();

        this.mockMvc.perform(put("/clients").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newClient))) // Сериализация объекта в JSON
                .andDo(print())
                .andExpect(status().isOk());
        //проверяем что данные поменялись
        this.mockMvc.perform(get("/clients/1").session(session)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Иван"))
                .andExpect(jsonPath("$.surname").value("Петров"))
                .andExpect(jsonPath("$.birthday").value("1990-01-10"))
                // телефон должен поменятся
                .andExpect(jsonPath("$.telephone").value("+11111111111"))
                .andExpect(jsonPath("$.email").value("ivan.petrov@example.com"))
                .andExpect(jsonPath("$.login").value("ivan90"))
                .andExpect(jsonPath("$.password").value("0000"));

    }

    @Test
    void updateOtherClientTest() throws Exception {
        //СОздаем сессию клиента с id 1
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "ivan90");
        session.setAttribute("userId", 1);

        this.mockMvc.perform(get("/clients/1").session(session)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Иван"))
                .andExpect(jsonPath("$.surname").value("Петров"))
                .andExpect(jsonPath("$.birthday").value("1990-01-10"))
                .andExpect(jsonPath("$.telephone").value("+11111111111"))
                .andExpect(jsonPath("$.email").value("ivan.petrov@example.com"))
                .andExpect(jsonPath("$.login").value("ivan90"))
                .andExpect(jsonPath("$.password").value("0000"));

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

        this.mockMvc.perform(put("/clients").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newClient))) // Сериализация объекта в JSON
                .andDo(print())
                .andExpect(status().isForbidden()); // Ожидаем ошибку 403 Forbidden, если пользователь не имеет прав
    }

    @Test
    void findClient() throws Exception {
        //СОздаем сессию клиента с id 1
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "ivan90");
        session.setAttribute("userId", 1);

        this.mockMvc.perform(get("/clients/1").session(session)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Иван"))
                .andExpect(jsonPath("$.surname").value("Петров"))
                .andExpect(jsonPath("$.birthday").value("1990-01-10"))
                .andExpect(jsonPath("$.telephone").value("+79007654321"))
                .andExpect(jsonPath("$.email").value("ivan.petrov@example.com"))
                .andExpect(jsonPath("$.login").value("ivan90"))
                .andExpect(jsonPath("$.password").value("0000"));
    }

    @Test
    void findOtherClientTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "ivan90");
        session.setAttribute("userId", 1);
        // пытаемся получить данные другого клиента
        this.mockMvc.perform(get("/clients/2").session(session)).andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void createClientWorkoutTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "ivan90");
        session.setAttribute("userId", 1);
//        создаем новую тренировку
        CreateWorkoutRequest newWorkout = CreateWorkoutRequest.builder()
                .clientId(1)
                .trainerId(2)
                .date(LocalDate.of(2025, 10, 10))
                .startTime(LocalTime.of(11, 0, 0))
                .build();

        this.mockMvc.perform(post("/workout")
                        .contentType(MediaType.APPLICATION_JSON).session(session)
                        .content(objectMapper.writeValueAsString(newWorkout)))// Сериализация объекта в JSON
                .andDo(print())
                .andExpect(status().isOk());

        // проверяем что тренировок стало больше
        this.mockMvc.perform(get("/clients/1/workouts").session(session)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void deleteClientWorkoutTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "ivan90");
        session.setAttribute("userId", 1);

        // удоляем тренировку
        this.mockMvc.perform(delete("/workout/1").session(session))
                .andExpect(status().isOk());

        // проверяем что у нас одна тренировка
        this.mockMvc.perform(get("/clients/1/workouts").session(session)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

    }
}
// 1. Клиент пытается изменить свои данные = ок (готово)
// 2. Клиент пытается изменить чужие данные = не написан(должна вернутся ошибка)(готово)
// 3. Проверить добавление и удоление тренировки(этот тест есть в тестах тренировок)
// 4. Может смотреть свои данные и не может смотреть данные другого клиента(готово)
