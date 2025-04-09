package com.example.TrialTraining;

import com.example.TrialTraining.workout.mapper.WorkoutRowMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {
    @Bean
    public WorkoutRowMapper workoutRowMapper() {
        WorkoutRowMapper w = new WorkoutRowMapper();
        return w;
    }
}
