package com.example.trial_training.service.client;

import com.example.trial_training.controller.client.CreateClientRequest;
import com.example.trial_training.dto.client.ClientDto;
import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.model.client.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    ClientDto create(Client newClient);//CreateClientRequest newClient

    List<ClientDto> findAllClients();

    ClientDto findClient(Integer id);

     Client updateClient(Client newClient);//Integer

    void deleteClient(Integer id);

    List<WorkoutDto> findAllWorkoutsOfClient(Integer id);

    Optional<ClientDto> findByLogin(String login);
}

