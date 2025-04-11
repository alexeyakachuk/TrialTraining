package com.example.TrialTraining.client.service;

import com.example.TrialTraining.client.dto.ClientDto;
import com.example.TrialTraining.client.model.Client;
import com.example.TrialTraining.client.myInterface.ClientInterface;
import com.example.TrialTraining.client.repository.ClientDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService implements ClientInterface {

    private final ClientInterface clientDbRepository;

    @Autowired
    public ClientService(ClientDbRepository clientDbRepository) {
        this.clientDbRepository = clientDbRepository;
    }

    @Override
    public ClientDto create(Client newClient) {
        return clientDbRepository.create(newClient);
    }

    @Override
    public List<ClientDto> findAllClient() {
        return List.of();
    }

    @Override
    public ClientDto findClient(Integer id) {
        return clientDbRepository.findClient(id);
    }
}
