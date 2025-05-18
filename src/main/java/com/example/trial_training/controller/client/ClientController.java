package com.example.trial_training.controller.client;

import com.example.trial_training.CreateClientRequest;
import com.example.trial_training.dto.client.ClientDto;
import com.example.trial_training.model.client.Client;
import com.example.trial_training.service.client.ClientService;
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
    public ClientDto create(@Valid @RequestBody CreateClientRequest newClient) {
        return clientService.create(newClient);
    }

    @GetMapping("/{id}")
    public ClientDto findClient(@PathVariable Integer id) {
        
        return clientService.findClient(id);
    }

    @GetMapping()
    public List<ClientDto> findAllClients() {
        return clientService.findAllClients();
    }

    @DeleteMapping("{id}")
    public void deleteClient(@PathVariable Integer id) {
        clientService.deleteClient(id);
    }

    @PutMapping
    public Integer updateClient(@Valid @RequestBody Client newClient) {
        return clientService.updateClient(newClient);
    }
}
