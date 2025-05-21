package com.example.trial_training.service.client;

import com.example.trial_training.controller.client.CreateClientRequest;
import com.example.trial_training.dto.client.ClientDto;
import com.example.trial_training.model.client.Client;
import com.example.trial_training.repository.client.ClientRepository;
import com.example.trial_training.repository.client.ClientDbRepository;
import com.example.trial_training.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientDbRepository;

    @Autowired
    public ClientServiceImpl(ClientDbRepository clientDbRepository) {
        this.clientDbRepository = clientDbRepository;
    }

    @Override
    public ClientDto create(CreateClientRequest newClient) {
        Client client = clientDbRepository.create(newClient);
        return new ClientDto(client);
    }

    @Override
    public List<ClientDto> findAllClients() {
         return clientDbRepository.findAllClients().stream()
                 .map(client -> new ClientDto(client))
                 .toList();
    }

    @Override
    public ClientDto findClient(Integer id) {
        Client client = clientDbRepository.findClient(id);
        if (client == null) {
            throw new NotFoundException("Клиент с id " + id + " не найден");
        }
        return new ClientDto(client);
    }

    @Override
    public Integer updateClient(Client newClient) {
        return clientDbRepository.updateClient(newClient);
    }

    @Override
    public void deleteClient(Integer id) {
        clientDbRepository.deleteClient(id);
    }
}
