package com.example.TrialTraining.client.controller;

import com.example.TrialTraining.client.dto.ClientDto;
import com.example.TrialTraining.client.model.Client;
import com.example.TrialTraining.client.myInterface.ClientInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClientController implements ClientInterface {

    private final ClientInterface clientService;

    @Autowired
    public ClientController(ClientInterface clientService) {
        this.clientService = clientService;
    }

    @Override
    @PostMapping
    public ClientDto create(@Valid @RequestBody Client newClient) {
        return clientService.create(newClient);
    }

    @Override
    public List<ClientDto> findAllClient() {
        return List.of();
    }

    @Override
    @GetMapping("/client/id")
    public ClientDto findClient(@PathVariable Integer id) {
        return clientService.findClient(id);
    }

}
