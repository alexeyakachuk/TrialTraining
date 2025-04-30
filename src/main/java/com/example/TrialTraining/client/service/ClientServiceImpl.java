package com.example.TrialTraining.client.service;

import com.example.TrialTraining.client.dto.ClientDto;
import com.example.TrialTraining.client.model.Client;
import com.example.TrialTraining.client.repository.ClientRepository;
import com.example.TrialTraining.client.repository.ClientDbRepository;
import com.example.TrialTraining.excepion.ConflictException;
import com.example.TrialTraining.excepion.NotFoundException;
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
    public ClientDto create(Client newClient) {
        Client client = clientDbRepository.create(newClient);
        return builderClient(client);
    }

//    @Override
//    public List<ClientDto> findAllClient() {
//        List<Client> clients = clientDbRepository.findAllClient();
//        List<ClientDto> allClient = new ArrayList<>();
//        for (Client client : clients) {
//            ClientDto clientDto = builderClient(client);
//            allClient.add(clientDto);
//        }
//        return allClient;
//    }

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
