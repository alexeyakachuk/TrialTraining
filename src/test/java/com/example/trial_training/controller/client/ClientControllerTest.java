package com.example.trial_training.controller.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private MockHttpSession session;
    private String str = "clients";

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute("username", "ivan90");
        session.setAttribute("userId", 1);
    }


//    @Test
//    void findAllClientsTest() throws Exception {
//        this.mockMvc.perform(get("/clients")).andDo(print()).andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(5));
//    }

    @Test
    void deleteClientTest() throws Exception {
        this.mockMvc.perform(delete("/" + str).session(session)).andDo(print())
                .andExpect(status().isOk());
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

}
