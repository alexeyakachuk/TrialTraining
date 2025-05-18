package com.example.trial_training.repository.client;

import com.example.trial_training.CreateClientRequest;
import com.example.trial_training.model.client.Client;

import java.util.List;

public interface ClientRepository {

    Client create(CreateClientRequest newClient);

    List<Client> findAllClients();

    Client findClient(Integer id);

    Integer updateClient(Client newClient);

    void deleteClient(Integer id);
}
