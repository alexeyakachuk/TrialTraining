package com.example.trial_training.controller.client;

import com.example.trial_training.dto.client.ClientDto;
import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.exception.AuthenticationException;
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
        //В следующей ветке доработать что бы мог смотреть тоько тренер которому записан на тренировку
        // этот клиент
//        final HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("userId") != id) {
//            throw new AuthenticationException("Не найдена сессия");
//        }
        return clientService.findClient(id);
    }

    @GetMapping()
    public List<ClientDto> findAllClients() {
        return clientService.findAllClients();
    }
    //добавить метод findAllClients(Integer id) id - тренера. Тренер должен иметь возможность
    // посмотреть всех своих клиентов. Спросить у Андрея писать этот метод у тренера или клиента

    @DeleteMapping("{id}")
    public void deleteClient(@PathVariable Integer id, HttpServletRequest request) {
//        final HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("userId") != id) {
//            throw new AuthenticationException("Не найдена сессия");
//        }
        clientService.deleteClient(id);
    }

    @PutMapping
    public Integer updateClient(@Valid @RequestBody Client newClient, HttpServletRequest request) {

//        HttpSession session = request.getSession(false);
//
//        if (session == null) {
//            throw new AuthenticationException("Не найдена сессия");
//        }
//        Object userId = session.getAttribute("userId");
//
//        if (!((userId) instanceof Integer)) {
//            throw new AuthenticationException("Некорректные данные сессии");
//        }
//
//        Integer id = (Integer) userId;
//
//        if (!id.equals(newClient.getId())) {
//            throw new AuthenticationException("Доступ запрещён: можно обновлять только свои данные");
//        }
        return clientService.updateClient(newClient);
    }

    @GetMapping("/{id}/workouts")
    public List<WorkoutDto> findAllWorkoutsOfClient(@PathVariable Integer id, HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") != id) {
            throw new AuthenticationException("Не найдена сессия");
        }
        return clientService.findAllWorkoutsOfClient(id);
    }
}