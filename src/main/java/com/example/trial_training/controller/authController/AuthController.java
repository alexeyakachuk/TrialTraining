package com.example.trial_training.controller.authController;

import com.example.trial_training.controller.authRequest.AuthRequest;
import com.example.trial_training.dto.client.ClientDto;
import com.example.trial_training.dto.trainer.TrainerDto;
import com.example.trial_training.service.client.ClientService;
import com.example.trial_training.service.trainer.TrainerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ClientService clientService;
    private final TrainerService trainerService;

    public AuthController(ClientService clientService, TrainerService trainerService) {
        this.clientService = clientService;
        this.trainerService = trainerService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest, HttpSession session) {
        Optional<ClientDto> clientOpt = clientService.findByLogin(authRequest.login());
        if (clientOpt.isPresent()) {
            ClientDto clientDto = clientOpt.get();
            if (clientDto.getPassword().equals(authRequest.password())) {
                session.setAttribute("username", clientDto.getLogin());
                session.setAttribute("userId", clientDto.getId());
                return ResponseEntity.ok("Успешный вход");
            }
        }

        Optional<TrainerDto> trainerOpt = trainerService.findByLogin(authRequest.login());
        if (trainerOpt.isPresent()) {
            TrainerDto trainerDto = trainerOpt.get();
            if (trainerDto.getPassword().equals(authRequest.password())) {
                session.setAttribute("username", trainerDto.getLogin());
                session.setAttribute("userId", trainerDto.getId());
                return ResponseEntity.ok("Успешный вход");
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин или пароль");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Вы вышли из системы");
    }
}

