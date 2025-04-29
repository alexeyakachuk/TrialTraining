package com.example.TrialTraining.trainer.repository;

import com.example.TrialTraining.trainer.mapper.TrainerRowMapper;
import com.example.TrialTraining.trainer.model.Trainer;
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
public class TrainerDbRepository implements TrainerRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TrainerRowMapper mapper;
    private final NamedParameterJdbcOperations jdbcOperations;

    private final String createSql = "INSERT INTO trainer (name, surname, birthday, telephone, email, login) " +
            "VALUES (:name, :surname, :birthday, :telephone, :email, :login)";

    private final String findSql = "SELECT * FROM trainer WHERE id = ?";

    private final String emailSql = "SELECT * FROM trainer WHERE email = ?";

    private final String findAllSql = "SELECT * FROM trainer";

    public TrainerDbRepository(JdbcTemplate jdbcTemplate, TrainerRowMapper mapper, NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Trainer create(Trainer newTrainer) {
        log.info("Создаем нового тренера : {}", newTrainer.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
//        params.addValue("id", newTrainer.getId());
        params.addValue("name", newTrainer.getName());
        params.addValue("surname", newTrainer.getSurname());
        params.addValue("birthday", newTrainer.getBirthday());
        params.addValue("telephone", newTrainer.getTelephone());
        params.addValue("email", newTrainer.getEmail());
        params.addValue("login", newTrainer.getLogin());

        jdbcOperations.update(createSql, params, keyHolder);

        log.info("Новый тренер {} создан", newTrainer.getName());

        Integer trainerId = Objects.requireNonNull(keyHolder.getKey()).intValue();

        return findTrainer(trainerId);
    }

    @Override
    public List<Trainer> findAllTrainer() {
        return jdbcTemplate.query(findAllSql, mapper);
    }

    @Override
    public Trainer findTrainer(Integer id) {
        try {
            return jdbcTemplate.queryForObject(findSql, mapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Integer updateTrainer(Trainer newTrainer) {
        String sql = "UPDATE trainer SET name = :name, surname = :surname, birthday = :birthday, " +
                "telephone = :telephone, email = :email, login = :login WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", newTrainer.getId());
        params.addValue("name", newTrainer.getName());
        params.addValue("surname", newTrainer.getSurname());
        params.addValue("birthday", newTrainer.getBirthday());
        params.addValue("telephone", newTrainer.getTelephone());
        params.addValue("email", newTrainer.getEmail());
        params.addValue("login", newTrainer.getLogin());

        return jdbcOperations.update(sql, params);
    }

    @Override
    public void deleteTrainer(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        String sql = "DELETE FROM trainer WHERE id = :id";

        jdbcOperations.update(sql, params);
    }

    @Override
    public Trainer checkEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(emailSql, mapper, email);
        } catch (DataAccessException e) {
            return null;
        }
    }
}
