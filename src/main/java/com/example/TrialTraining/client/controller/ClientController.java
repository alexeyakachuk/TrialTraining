package com.example.TrialTraining.client.controller;

import com.example.TrialTraining.client.dto.ClientDto;
import com.example.TrialTraining.client.model.Client;
import com.example.TrialTraining.client.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    @PostMapping
    public ClientDto create(@Valid @RequestBody Client newClient) {
        return clientService.create(newClient);
    }

    @GetMapping("/{id}")
    public ClientDto findClient(@PathVariable Integer id) {
        
        return clientService.findClient(id);
    }

    @GetMapping()
    public List<ClientDto> findAllClients() {
        return clientService.findAllClient();
    }
}
