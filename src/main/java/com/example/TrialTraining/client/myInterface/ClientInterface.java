package com.example.TrialTraining.client.myInterface;

import com.example.TrialTraining.client.dto.ClientDto;
import com.example.TrialTraining.client.model.Client;

import java.util.List;

public interface ClientInterface {

    ClientDto create(Client newClient);

    List<ClientDto> findAllClient();

    ClientDto findClient(Integer id);
}
