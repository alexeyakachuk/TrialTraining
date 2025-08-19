package com.example.trial_training.controller.workout;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class WorkoutControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    //    private MockHttpSession session;
    private String str = "workout";


    @Test
    void createWorkoutClientTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "ivan90");
        session.setAttribute("userId", 1);

        CreateWorkoutRequest newWorkout = CreateWorkoutRequest.builder()
                .clientId(1)
                .trainerId(2)
                .date(LocalDate.of(2025, 10, 10))
                .startTime(LocalTime.of(11, 0, 0))
                .build();
        this.mockMvc.perform(post("/" + str)
                        .contentType(MediaType.APPLICATION_JSON).session(session)
                        .content(objectMapper.writeValueAsString(newWorkout)))// Сериализация объекта в JSON
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void createWorkoutTrainerTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "marina85");
        session.setAttribute("userId", 2);

        CreateWorkoutRequest newWorkout = CreateWorkoutRequest.builder()
                .clientId(1)
                .trainerId(2)
                .date(LocalDate.of(2025, 10, 10))
                .startTime(LocalTime.of(12, 0, 0))
                .build();
        this.mockMvc.perform(post("/" + str)
                        .contentType(MediaType.APPLICATION_JSON).session(session)
                        .content(objectMapper.writeValueAsString(newWorkout)))// Сериализация объекта в JSON
                .andDo(print())
                .andExpect(status().isOk());
    }
//разобратся с тестами не нравится как работает
    @Test
    void findWorkoutClientTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "ivan90");
        session.setAttribute("userId", 1);

        this.mockMvc.perform(get("/" + str + "/1")
                        .session(session))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.client.id").value(1))
                .andExpect(jsonPath("$.trainer.id").value(1))
                .andExpect(jsonPath("$.date").value("2024-06-01"))
                .andExpect(jsonPath("$.startTime").value("09:00:00"))
                .andExpect(jsonPath("$.endTime").value("10:00:00"));
    }

    @Test
    void findWorkoutTrainerTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "marina85");
        session.setAttribute("userId", 2);

        this.mockMvc.perform(get("/" + str + "/3")
                        .session(session))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.client.id").value(3))
                .andExpect(jsonPath("$.trainer.id").value(2))
                .andExpect(jsonPath("$.date").value("2024-06-01"))
                .andExpect(jsonPath("$.startTime").value("10:00:00"))
                .andExpect(jsonPath("$.endTime").value("11:00:00"));
    }

    @Test
    void findAllWorkoutClientTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "ivan90");
        session.setAttribute("userId", 1);

        this.mockMvc.perform(get("/" +str).session(session))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5))) // Проверяем, что возвращается 5 тренировок
                .andExpect(jsonPath("$[0].client.id").value(1))
                .andExpect(jsonPath("$[0].trainer.id").value(1))
                .andExpect(jsonPath("$[0].date").value("2024-06-01"))
                .andExpect(jsonPath("$[0].startTime").value("09:00:00"))
                .andExpect(jsonPath("$[0].endTime").value("10:00:00"))

                .andExpect(jsonPath("$[1].client.id").value(2))
                .andExpect(jsonPath("$[1].trainer.id").value(1))
                .andExpect(jsonPath("$[1].date").value("2024-06-02"))
                .andExpect(jsonPath("$[1].startTime").value("11:00:00"))
                .andExpect(jsonPath("$[1].endTime").value("12:00:00"))

                .andExpect(jsonPath("$[2].client.id").value(3))
                .andExpect(jsonPath("$[2].trainer.id").value(2))
                .andExpect(jsonPath("$[2].date").value("2024-06-01"))
                .andExpect(jsonPath("$[2].startTime").value("10:00:00"))
                .andExpect(jsonPath("$[2].endTime").value("11:00:00"))

                .andExpect(jsonPath("$[3].client.id").value(4))
                .andExpect(jsonPath("$[3].trainer.id").value(2))
                .andExpect(jsonPath("$[3].date").value("2024-06-03"))
                .andExpect(jsonPath("$[3].startTime").value("14:00:00"))
                .andExpect(jsonPath("$[3].endTime").value("15:00:00"))

                .andExpect(jsonPath("$[4].client.id").value(5))
                .andExpect(jsonPath("$[4].trainer.id").value(2))
                .andExpect(jsonPath("$[4].date").value("2024-06-04"))
                .andExpect(jsonPath("$[4].startTime").value("16:00:00"))
                .andExpect(jsonPath("$[4].endTime").value("17:00:00"));
    }

    @Test
    void findAllWorkoutTrainerTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "marina85");
        session.setAttribute("userId", 2);

        this.mockMvc.perform(get("/" +str).session(session))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5))) // Проверяем, что возвращается 5 тренировок
                .andExpect(jsonPath("$[0].client.id").value(1))
                .andExpect(jsonPath("$[0].trainer.id").value(1))
                .andExpect(jsonPath("$[0].date").value("2024-06-01"))
                .andExpect(jsonPath("$[0].startTime").value("09:00:00"))
                .andExpect(jsonPath("$[0].endTime").value("10:00:00"))

                .andExpect(jsonPath("$[1].client.id").value(2))
                .andExpect(jsonPath("$[1].trainer.id").value(1))
                .andExpect(jsonPath("$[1].date").value("2024-06-02"))
                .andExpect(jsonPath("$[1].startTime").value("11:00:00"))
                .andExpect(jsonPath("$[1].endTime").value("12:00:00"))

                .andExpect(jsonPath("$[2].client.id").value(3))
                .andExpect(jsonPath("$[2].trainer.id").value(2))
                .andExpect(jsonPath("$[2].date").value("2024-06-01"))
                .andExpect(jsonPath("$[2].startTime").value("10:00:00"))
                .andExpect(jsonPath("$[2].endTime").value("11:00:00"))

                .andExpect(jsonPath("$[3].client.id").value(4))
                .andExpect(jsonPath("$[3].trainer.id").value(2))
                .andExpect(jsonPath("$[3].date").value("2024-06-03"))
                .andExpect(jsonPath("$[3].startTime").value("14:00:00"))
                .andExpect(jsonPath("$[3].endTime").value("15:00:00"))

                .andExpect(jsonPath("$[4].client.id").value(5))
                .andExpect(jsonPath("$[4].trainer.id").value(2))
                .andExpect(jsonPath("$[4].date").value("2024-06-04"))
                .andExpect(jsonPath("$[4].startTime").value("16:00:00"))
                .andExpect(jsonPath("$[4].endTime").value("17:00:00"));
    }

    @Test
    void deleteWorkoutClientTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "ivan90");
        session.setAttribute("userId", 1);

        this.mockMvc.perform(delete("/" + str + "/1").session(session)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteWorkoutTrainerTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "marina85");
        session.setAttribute("userId", 2);

        this.mockMvc.perform(delete("/" + str + "/2").session(session)).andDo(print())
                .andExpect(status().isOk());
    }
}
