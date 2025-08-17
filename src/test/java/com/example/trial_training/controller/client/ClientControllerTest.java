package com.example.trial_training.controller.client;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
//разобратся с этими аннотациями
@Transactional
@Rollback
public class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private MockHttpSession session;
    private String str = "clients";

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute("username", "ivan90");
        session.setAttribute("userId", 1);
    }


    @Test
    void findAllClientsTest() throws Exception {
        this.mockMvc.perform(get("/clients").session(session))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void findAllWorkoutsOfClientTest() throws Exception {
        this.mockMvc.perform(get("/" + str + "/1/workouts").session(session)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findClientTest() throws Exception {
        this.mockMvc.perform(get("/" + str + "/1").session(session)).andDo(print())
                .andDo(print())
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
    void createTest() throws Exception {
        CreateClientRequest newClient = CreateClientRequest.builder()
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
                        .content(objectMapper.writeValueAsString(newClient))) // Сериализация объекта в JSON
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateTest() throws Exception {
        Client newClient = Client.builder()
                .id(1)
                .name("Миша")
                .surname("Михайлов")
                .birthday(LocalDate.of(1990, 1, 10))
                .telephone("+79901234578") // изменил номер
                .email("mihail.yandex@org.ru")
                .login("miha01")
                .password("9999")
                .build();

        this.mockMvc.perform(put("/" + str).session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newClient))) // Сериализация объекта в JSON
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/" + str + "/1").session(session)).andDo(print())
        .andExpect(jsonPath("$.name").value("Миша"))
                .andExpect(jsonPath("$.surname").value("Михайлов"))
                .andExpect(jsonPath("$.birthday").value("1990-01-10"))
                .andExpect(jsonPath("$.telephone").value("+79901234578"))
                .andExpect(jsonPath("$.email").value("mihail.yandex@org.ru"))
                .andExpect(jsonPath("$.login").value("miha01"))
                .andExpect(jsonPath("$.password").value("9999"));
    }

    @Test
    void deleteClientTest() throws Exception {
        this.mockMvc.perform(delete("/" + str).session(session)).andDo(print())
                .andExpect(status().isOk());
    }
}
//спросить почему падают тесты