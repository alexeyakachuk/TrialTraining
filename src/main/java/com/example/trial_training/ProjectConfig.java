package com.example.trial_training;

import com.example.trial_training.filters.ClientFilter;
import com.example.trial_training.filters.TrainerFilter;
import com.example.trial_training.filters.WorkoutFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    public FilterRegistrationBean<ClientFilter> clientFilterRegistration(ClientFilter clientFilter) {
        FilterRegistrationBean<ClientFilter> registration = new FilterRegistrationBean<>(clientFilter);
        registration.addUrlPatterns("/clients", "/clients/*");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<TrainerFilter> trainerFilterRegistration(TrainerFilter trainerFilter) {
        FilterRegistrationBean<TrainerFilter> registration = new FilterRegistrationBean<>(trainerFilter);
        registration.addUrlPatterns("/trainers", "/trainers/*");
        registration.setOrder(2);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<WorkoutFilter> workoutFilterRegistration(WorkoutFilter workoutFilter) {
        FilterRegistrationBean<WorkoutFilter> registration = new FilterRegistrationBean<>(workoutFilter);
        registration.addUrlPatterns("/workout", "/workout/*");
        registration.setOrder(3);
        return registration;
    }
}
