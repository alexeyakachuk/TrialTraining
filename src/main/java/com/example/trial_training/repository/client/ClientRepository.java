package com.example.trial_training.repository.client;

import com.example.trial_training.model.client.Client;

import java.util.List;

public interface ClientRepository {

    Client create(Client newClient);

    List<Client> findAllClient();

    Client findClient(Integer id);

    Integer updateClient(Client newClient);

    void deleteClient(Integer id);
}
