package com.example.trial_training.repository.client;

import com.example.trial_training.controller.client.CreateClientRequest;
import com.example.trial_training.model.client.Client;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ClientDbRepositoryTest {
    private final ClientRepository clientRepository;
    private final NamedParameterJdbcOperations jdbcOperations;
    private Client testClient1;

    @BeforeEach
    void setUp() {
        jdbcOperations.update("DELETE FROM client", Map.of());

        CreateClientRequest newClient1 = CreateClientRequest.builder()
                .name("client1")
                .surname("surname1")
                .birthday(LocalDate.of(1991,10,10))
                .telephone("1111")
                .email("1@y.ru")
                .login("1111")
                .password("aaaa")
                .build();

        testClient1 = clientRepository.create(newClient1);

        CreateClientRequest newClient2 = CreateClientRequest.builder()
                .name("client2")
                .surname("surname2")
                .birthday(LocalDate.of(1992,10,10))
                .telephone("2222")
                .email("2@y.ru")
                .login("2222")
                .password("ssss")
                .build();

        clientRepository.create(newClient2);

    }

    @Test
    void createClientTest() {
        CreateClientRequest newClient = CreateClientRequest.builder()
                .name("client")
                .surname("surname")
                .birthday(LocalDate.of(1990,10,10))
                .telephone("0000")
                .email("0@y.ru")
                .login("0000")
                .password("dddd")
                .build();

        Client client = clientRepository.create(newClient);

        assertNotNull(client.getId());
        assertEquals(newClient.getName(), client.getName(), "Имя должно совподать");
        assertEquals(newClient.getSurname(), client.getSurname(), "Фамилия должна совподать");
        assertEquals(newClient.getBirthday(), client.getBirthday(), "Дата рождения должна совподать");
        assertEquals(newClient.getTelephone(), client.getTelephone(), "Номер телефона должен совподать");
        assertEquals(newClient.getEmail(), client.getEmail(), "email должен совподать");
        assertEquals(newClient.getLogin(), client.getLogin(), "login должен совподать");
    }

    @Test
    void findClientIdTest() {
        Client client = clientRepository.findClient(testClient1.getId());

        assertNotNull(client);
        assertEquals(testClient1, client, "Клиенты должны совподать");
    }

    @Test
    void findAllClientsTest() {
        List<Client> allClient = clientRepository.findAllClients();

        assertEquals(2, allClient.size());
    }

    @Test
    void deleteClientTest() {
        clientRepository.deleteClient(testClient1.getId());
        List<Client> allClient = clientRepository.findAllClients();

        assertEquals(1, allClient.size());
    }

    @Test
    void updateClientTest() {
        System.out.println(testClient1.getTelephone());
        testClient1.setTelephone("3333");
        clientRepository.updateClient(testClient1);
        Client client = clientRepository.findClient(testClient1.getId());

        System.out.println(client.getTelephone());
        assertEquals(testClient1.getId(), client.getId());
        assertEquals(testClient1.getTelephone(), client.getTelephone());
    }
}
