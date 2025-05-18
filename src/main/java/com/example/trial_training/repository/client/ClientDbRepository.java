package com.example.trial_training.repository.client;

import com.example.trial_training.CreateClientRequest;
import com.example.trial_training.mapper.client.ClientRowMapper;
import com.example.trial_training.model.client.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
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
    private final JdbcTemplate jdbcTemplate;
    private final ClientRowMapper mapper;
    private final NamedParameterJdbcOperations jdbcOperations;

    public ClientDbRepository(JdbcTemplate jdbcTemplate, ClientRowMapper mapper,
                              NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Client create(CreateClientRequest newClient) {
        log.info("Создаем нового клиента : {}", newClient.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", newClient.getName());
        params.addValue("surname", newClient.getSurname());
        params.addValue("birthday", newClient.getBirthday());
        params.addValue("telephone", newClient.getTelephone());
        params.addValue("email", newClient.getEmail());
        params.addValue("login", newClient.getLogin());

        String sql = "INSERT INTO client (name, surname, birthday, telephone, email, login) " +
                "VALUES (:name, :surname, :birthday, :telephone, :email, :login)";
        jdbcOperations.update(sql, params, keyHolder);

        log.info("Новый клиент {} создан", newClient.getName());

        Integer clientId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return findClient(clientId);
    }

    @Override
    public List<Client> findAllClients() {
        String sql = "SELECT * FROM client";

        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public Client findClient(Integer id) {
        String sql = "SELECT * FROM client WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, mapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Integer updateClient(Client newClient) {
        String sql = "UPDATE client SET name = :name, surname = :surname, birthday = :birthday, " +
                "telephone = :telephone, email = :email, login = :login WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", newClient.getId());
        params.addValue("name", newClient.getName());
        params.addValue("surname", newClient.getSurname());
        params.addValue("birthday", newClient.getBirthday());
        params.addValue("telephone", newClient.getTelephone());
        params.addValue("email", newClient.getEmail());
        params.addValue("login", newClient.getLogin());

        return jdbcOperations.update(sql, params);
    }

    @Override
    public void deleteClient(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        String sql = "DELETE FROM client WHERE id = :id";

        jdbcOperations.update(sql, params);
    }
}
