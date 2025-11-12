package com.example.trial_training.service.client;

import com.example.trial_training.controller.client.CreateClientRequest;
import com.example.trial_training.dto.client.ClientDto;
import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.model.client.Client;
import com.example.trial_training.repository.client.ClientDbRepository;
import com.example.trial_training.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientDbRepository clientDbRepository;

    @Autowired
    public ClientServiceImpl(ClientDbRepository clientDbRepository) {
        this.clientDbRepository = clientDbRepository;
    }

    @Override
    public List<ClientDto> findAllClients() {
        return clientDbRepository.findByUsers().stream()
                .map(client -> new ClientDto(client))
                .toList();
    }

    @Override
    public ClientDto findClient(Integer id) {
        Client client = clientDbRepository.findById(id);
        if (client == null) {
            throw new NotFoundException("Клиент с id " + id + " не найден");
        }
        return new ClientDto(client);
    }

    @Override
    public ClientDto create(Client newClient) {
        Client client = clientDbRepository.create(newClient);
        return new ClientDto(client);
    }


    // не помню где используется потом обязательно разаобратся
    @Override
    public Optional<ClientDto> findByLogin(String login) {
        Client client = clientDbRepository.findByLogin(login);
        return Optional.ofNullable(client).map(ClientDto::new);
    }

    @Override
    public Client updateClient(Client newClient) {
        return clientDbRepository.update(newClient);
    }

    @Override
    public void deleteClient(Integer id) {
        clientDbRepository.delete(id);
    }

    @Override
    public List<WorkoutDto> findAllWorkoutsOfClient(Integer id) {
        return clientDbRepository.findAllWorkoutsOfClient(id);
    }
}
