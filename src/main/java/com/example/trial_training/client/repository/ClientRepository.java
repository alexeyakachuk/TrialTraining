package com.example.trial_training.client.repository;

import com.example.trial_training.client.model.Client;

import java.util.List;

public interface ClientRepository {

    Client create(Client newClient);

    List<Client> findAllClient();

    Client findClient(Integer id);

    Integer updateClient(Client newClient);

    void deleteClient(Integer id);
}
