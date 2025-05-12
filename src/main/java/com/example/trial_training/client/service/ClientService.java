package com.example.trial_training.client.service;

import com.example.trial_training.client.dto.ClientDto;
import com.example.trial_training.client.model.Client;

import java.util.List;

public interface ClientService {

    ClientDto create(Client newClient);

    List<ClientDto> findAllClient();

    ClientDto findClient(Integer id);

    Integer updateClient(Client newClient);

    void deleteClient(Integer id);
}
