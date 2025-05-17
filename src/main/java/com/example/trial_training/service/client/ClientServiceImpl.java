package com.example.trial_training.service.client;

import com.example.trial_training.CreateClientRequest;
import com.example.trial_training.dto.client.ClientDto;
import com.example.trial_training.model.client.Client;
import com.example.trial_training.repository.client.ClientRepository;
import com.example.trial_training.repository.client.ClientDbRepository;
import com.example.trial_training.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        return builderClient(client);
    }

    @Override
    public List<ClientDto> findAllClient() {
        return clientDbRepository.findAllClient().stream()
                .map(client -> builderClient(client)) // Преобразуем каждый Client в ClientDto
                .collect(Collectors.toList()); // Собираем результаты в список
    }

    @Override
    public ClientDto findClient(Integer id) {
        Client client = clientDbRepository.findClient(id);
        if (client == null) {
            throw new NotFoundException("Клиент с id " + id + " не найден");
        }
        return builderClient(client);
    }

    @Override
    public Integer updateClient(Client newClient) {
        return clientDbRepository.updateClient(newClient);
    }

    @Override
    public void deleteClient(Integer id) {
        clientDbRepository.deleteClient(id);
    }

    private ClientDto builderClient(Client client) {
        return ClientDto.builder()
                .name(client.getName())
                .surname(client.getSurname())
                .birthday(client.getBirthday())
                .telephone(client.getTelephone())
                .email(client.getEmail())
                .build();
    }
}
