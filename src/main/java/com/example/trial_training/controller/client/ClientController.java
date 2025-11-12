package com.example.trial_training.controller.client;

import com.example.trial_training.dto.client.ClientDto;
import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.exception.AuthenticationException;
import com.example.trial_training.filters.AuthFilters;
import com.example.trial_training.model.client.Client;
import com.example.trial_training.service.client.ClientService;
import com.example.trial_training.service.client.ClientServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/clients")
public class ClientController {

    private final ClientServiceImpl clientServiceImpl;

    @Autowired
    public ClientController(ClientServiceImpl clientServiceImpl) {
        this.clientServiceImpl = clientServiceImpl;
    }

    @PostMapping
    public ClientDto create(@Valid @RequestBody Client newClient) {
        return clientServiceImpl.create(newClient);
    }
// этот метод готов
    @GetMapping("/{id}")
    public ClientDto findClient(@PathVariable Integer id, HttpServletRequest request) {
        //В следующей ветке доработать что бы мог смотреть тоько тренер которому записан на тренировку
        // этот клиент
        return clientServiceImpl.findClient(id);
    }

    @GetMapping()
    public List<ClientDto> findAllClients() {
        return clientServiceImpl.findAllClients();
    }
    //добавить метод findAllClients(Integer id) id - тренера. Тренер должен иметь возможность
    // посмотреть всех своих клиентов. Спросить у Андрея писать этот метод у тренера или клиента

// этот метод готов
    @DeleteMapping//("/{id}")
    public void deleteClient(HttpServletRequest request) {//,@PathVariable Integer id
        Integer sessionUserId = AuthFilters.getSessionUserId(request);

        clientServiceImpl.deleteClient(sessionUserId);
//        clientServiceImpl.deleteClient(id);
    }

    @PutMapping
    public Client updateClient(@Valid @RequestBody Client newClient) {
        return clientServiceImpl.updateClient(newClient);
    }

    @GetMapping("/{id}/workouts")
    public List<WorkoutDto> findAllWorkoutsOfClient(@PathVariable Integer id) {
        return clientServiceImpl.findAllWorkoutsOfClient(id);
    }
}