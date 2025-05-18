package com.example.trial_training.service.client;

import com.example.trial_training.CreateClientRequest;
import com.example.trial_training.dto.client.ClientDto;
import com.example.trial_training.model.client.Client;

import java.util.List;

public interface ClientService {

    ClientDto create(CreateClientRequest newClient);

    List<ClientDto> findAllClients();

    ClientDto findClient(Integer id);

    Integer updateClient(Client newClient);

    void deleteClient(Integer id);
}
