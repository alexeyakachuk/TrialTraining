package com.example.trial_training.controller.trainer;

import com.example.trial_training.model.trainer.Trainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
public class TrainerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private MockHttpSession session;
    private String str = "trainers";

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute("username", "marina85");
        session.setAttribute("userId", 2);
    }

    @Test
    void findAllTrainersTest() throws Exception {
        this.mockMvc.perform(get("/trainers")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void findTrainerTest() throws Exception {
        this.mockMvc.perform(get("/trainers/1"))
                .andDo(print())
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
    void createTest() throws Exception {
        CreateTrainerRequest newTrainer = CreateTrainerRequest.builder()
                .name("Миша")
                .surname("Михайлов")
                .birthday(LocalDate.of(1990, 1, 10))
                .telephone("+89901234578")
                .email("mihail.yandex@org.ru")
                .login("miha01")
                .password("9999")
                .build();
        this.mockMvc.perform(post("/" + str)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTrainer))) // Сериализация объекта в JSON
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findAllTrainerWorkoutsTest() throws Exception {
        this.mockMvc.perform(get("/" + str + "/2/workout").session(session)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateTest() throws Exception {
        Trainer newTrainer = Trainer.builder()
                .id(2)
                .name("Марина")
                .surname("Кузнецова")
                .birthday(LocalDate.of(1985, 9, 20))
                .telephone("+8961876543") //ищменил номер
                .email("marina.kuznetsova@example.com")
                .login("marina85")
                .password("2222")
                .build();

        this.mockMvc.perform(put("/" + str).session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTrainer))) // Сериализация объекта в JSON
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/" + str + "/2").session(session)).andDo(print())
                .andExpect(jsonPath("$.name").value("Марина"))
                .andExpect(jsonPath("$.surname").value("Кузнецова"))
                .andExpect(jsonPath("$.birthday").value("1985-09-20"))
                .andExpect(jsonPath("$.telephone").value("+8961876543"))
                .andExpect(jsonPath("$.email").value("marina.kuznetsova@example.com"))
                .andExpect(jsonPath("$.login").value("marina85"))
                .andExpect(jsonPath("$.password").value("2222"));
    }

    @Test
    void deleteTest()throws Exception{
        this.mockMvc.perform(delete("/" + str).session(session)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findAllWorkoutsOfTrainerTest() throws Exception {
        this.mockMvc.perform(get("/" + str + "/2/workouts").session(session)).andDo(print())
                .andExpect(status().isOk());
    }
}
