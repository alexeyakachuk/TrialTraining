package com.example.TrialTraining.client.service;

import com.example.TrialTraining.client.dto.ClientDto;
import com.example.TrialTraining.client.model.Client;
import com.example.TrialTraining.client.repository.ClientRepository;
import com.example.TrialTraining.client.repository.ClientDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        return ClientDto.builder()
                .name(client.getName())
                .surname(client.getSurname())
                .birthday(client.getBirthday())
                .telephone(client.getTelephone())
                .email(client.getEmail())
                .build();
    }

    @Override
    public List<ClientDto> findAllClient() {
        List<Client> clients = clientDbRepository.findAllClient();
        List<ClientDto> allClient = new ArrayList<>();
        for (Client client : clients) {
            ClientDto clientDto = ClientDto.builder()
                    .name(client.getName())
                    .surname(client.getSurname())
                    .birthday(client.getBirthday())
                    .telephone(client.getTelephone())
                    .email(client.getEmail())
                    .build();
            allClient.add(clientDto);
        }
        return allClient;
    }

    @Override
    public ClientDto findClient(Integer id) {
        Client client = clientDbRepository.findClient(id);
        return ClientDto.builder()
                .name(client.getName())
                .surname(client.getSurname())
                .birthday(client.getBirthday())
                .telephone(client.getTelephone())
                .email(client.getEmail())
                .build();    }
}
