package com.example.trial_training.service.client;

import com.example.trial_training.controller.client.CreateClientRequest;
import com.example.trial_training.dto.client.ClientDto;
import com.example.trial_training.model.client.Client;
import com.example.trial_training.repository.client.ClientDbRepository;
import com.example.trial_training.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl {

    private final ClientDbRepository clientDbRepository;

    @Autowired
    public ClientServiceImpl(ClientDbRepository clientDbRepository) {
        this.clientDbRepository = clientDbRepository;
    }


    public List<ClientDto> findAllClients() {
        return clientDbRepository.findByUsers().stream()
                .map(client -> new ClientDto(client))
                .toList();
    }


    public ClientDto findClient(Integer id) {
        Client client = clientDbRepository.findById(id);
        if (client == null) {
            throw new NotFoundException("Клиент с id " + id + " не найден");
        }
        return new ClientDto(client);
    }

    public ClientDto create(Client newClient) {
        Client client = clientDbRepository.create(newClient);
        return new ClientDto(client);
    }


//    @Override
//    public Optional<ClientDto> findByLogin(String login) {
//        Client client = clientDbRepository.findClientByLogin(login);
//        return Optional.ofNullable(client).map(ClientDto::new);
//    }
//
//    @Override
//    public Integer updateClient(Client newClient) {
//        return clientDbRepository.updateClient(newClient);
//    }
//
//    @Override
//    public void deleteClient(Integer id) {
//        clientDbRepository.deleteClient(id);
//    }
//
//    @Override
//    public List<WorkoutDto> findAllWorkoutsOfClient(Integer id) {
//        return clientDbRepository.findAllWorkoutsOfClient(id);
//    }
}
