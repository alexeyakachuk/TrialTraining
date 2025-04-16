package com.example.TrialTraining.client.repository;

import com.example.TrialTraining.client.mapper.ClientRowMapper;
import com.example.TrialTraining.client.model.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
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
    private Integer id;

    private final JdbcTemplate jdbcTemplate;
    private final ClientRowMapper mapper;
    private final NamedParameterJdbcOperations jdbcOperations;

    private final String CREATE_SQL = "INSERT INTO client (name, surname, birthday, telephone, email, login) " +
            "VALUES (:name, :surname, :birthday, :telephone, :email, :login)";

    private final String findSql = "SELECT * FROM client WHERE id = ?";

    private final String emailSql = "SELECT * FROM client WHERE email = ?";

    private final String findAllSql = "SELECT * FROM client";


    public ClientDbRepository(JdbcTemplate jdbcTemplate, ClientRowMapper mapper,
                              NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Client create(Client newClient) {
        log.info("Создаем нового клиента : {}", newClient.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
//        params.addValue("id", newClient.getId());
        params.addValue("name", newClient.getName());
        params.addValue("surname", newClient.getSurname());
        params.addValue("birthday", newClient.getBirthday());
        params.addValue("telephone", newClient.getTelephone());
        params.addValue("email", newClient.getEmail());
        params.addValue("login", newClient.getLogin());


        jdbcOperations.update(CREATE_SQL, params, keyHolder);

        log.info("Новый клиент {} создан", newClient.getName());

        Integer clientId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return findClient(clientId);
    }

    @Override
    public List<Client> findAllClient() {

        return jdbcTemplate.query(findAllSql, mapper);
    }

    @Override
    public Client findClient(Integer id) {
        try {
            return jdbcTemplate.queryForObject(findSql, mapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Client checkEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(emailSql, mapper, email);
        } catch (DataAccessException e) {
            return null;
        }
    }



}
