package com.example.TrialTraining.client.repository;

import com.example.TrialTraining.client.dto.ClientDto;
import com.example.TrialTraining.client.mapper.ClientRowMapper;
import com.example.TrialTraining.client.model.Client;
import com.example.TrialTraining.client.myInterface.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class ClientDbRepository implements ClientRepository {

    private final JdbcTemplate template;
    private final ClientRowMapper mapper;
    private final NamedParameterJdbcOperations jdbcOperations;

    private final String createSql = "INSERT INTO client (name, surname, birthday, telephone, email, login) " +
            "VALUES (:name, :surname, :birthday, :telephone, :email, :login)";

    private final String findSql = "SELECT * FROM client WHERE id = :id";



    public ClientDbRepository(JdbcTemplate template, ClientRowMapper mapper,
                              NamedParameterJdbcOperations jdbcOperations) {
        this.template = template;
        this.mapper = mapper;
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public ClientDto create(Client newClient) {
        log.info("Создаем нового клиента : {}", newClient);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", newClient.getName());
        params.addValue("surname", newClient.getSurname());
        params.addValue("birthday", newClient.getBirthday());
        params.addValue("telephone", newClient.getTelephone());
        params.addValue("email", newClient.getEmail());
        params.addValue("login", newClient.getLogin());



        jdbcOperations.update(createSql, params, keyHolder);

        log.info("Новый клиент {} создан", newClient);

        Integer clientId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return findClient(clientId);
    }

    @Override
    public List<ClientDto> findAllClient() {
        return List.of();
    }

    @Override
    public ClientDto findClient(Integer id) {

        return template.queryForObject(findSql, mapper, id);
    }
}
