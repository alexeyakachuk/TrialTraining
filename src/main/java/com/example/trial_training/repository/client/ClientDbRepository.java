package com.example.trial_training.repository.client;

import com.example.trial_training.controller.client.CreateClientRequest;
import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.mapper.client.AllWorkoutOfClientDtoRowMapper;
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
    private final AllWorkoutOfClientDtoRowMapper allWorkoutDtoRowMapper;

    public ClientDbRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcOperations jdbcOperations,
                              ClientRowMapper mapper, AllWorkoutOfClientDtoRowMapper allWorkoutDtoRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcOperations = jdbcOperations;
        this.mapper = mapper;
        this.allWorkoutDtoRowMapper = allWorkoutDtoRowMapper;
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
        params.addValue("password", newClient.getPassword());

        String sql = "INSERT INTO client (name, surname, birthday, telephone, email, login, password) " +
                "VALUES (:name, :surname, :birthday, :telephone, :email, :login, :password)";
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
    public Client findClientByLogin(String login) {
        String sql = "SELECT * FROM client WHERE login = ?";

        try {
            return jdbcTemplate.queryForObject(sql, mapper, login);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Integer updateClient(Client newClient) {
        String sql = "UPDATE client SET name = :name, surname = :surname, birthday = :birthday, " +
                "telephone = :telephone, email = :email, login = :login, password = :password WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", newClient.getId());
        params.addValue("name", newClient.getName());
        params.addValue("surname", newClient.getSurname());
        params.addValue("birthday", newClient.getBirthday());
        params.addValue("telephone", newClient.getTelephone());
        params.addValue("email", newClient.getEmail());
        params.addValue("login", newClient.getLogin());
        params.addValue("password", newClient.getPassword());

        return jdbcOperations.update(sql, params);
    }

    @Override
    public void deleteClient(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        String sql = "DELETE FROM client WHERE id = :id";

        jdbcOperations.update(sql, params);
    }

    @Override
    public List<WorkoutDto> findAllWorkoutsOfClient(Integer id) {

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        String sql = "SELECT TRAINER.name, TRAINER.surname, TRAINER.telephone, WORKOUT.id, WORKOUT.\"date\", " +
                "WORKOUT.start_time, WORKOUT.end_time " +
                "FROM WORKOUT " +
                "LEFT JOIN TRAINER ON WORKOUT.trainer_id = TRAINER.id " + // Исправлено: сначала соединяем с TRAINER
                "LEFT JOIN CLIENT ON WORKOUT.client_id = CLIENT.id " + // Затем соединяем с CLIENT
                "WHERE WORKOUT.client_id = :id";

        return jdbcOperations.query(sql, params, allWorkoutDtoRowMapper);
    }
}
