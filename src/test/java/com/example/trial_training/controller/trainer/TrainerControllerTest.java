package com.example.trial_training.controller.trainer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class TrainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
}
