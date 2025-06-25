package com.example.trial_training.controller.client;

import com.example.trial_training.dto.client.ClientDto;
import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.model.client.Client;
import com.example.trial_training.service.client.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping(path = "/clients", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
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
    public ClientDto findClient(@PathVariable Integer id, HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") != id) {
            throw new IllegalStateException("Не найдена сессия");
        }
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

    @GetMapping("/{id}/workouts")
    public List<WorkoutDto> findAllWorkoutsOfClient(@PathVariable Integer id, HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") != id) {
            throw new IllegalStateException("Не найдена сессия");
        }
        return clientService.findAllWorkoutsOfClient(id);
    }
}