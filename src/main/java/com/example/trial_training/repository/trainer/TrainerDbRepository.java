package com.example.trial_training.repository.trainer;

import com.example.trial_training.controller.trainer.CreateTrainerRequest;
import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.mapper.trainer.AllWorkoutOfTrainerDtoRowMapper;
import com.example.trial_training.mapper.trainer.TrainerRowMapper;
import com.example.trial_training.model.trainer.Trainer;
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
public class TrainerDbRepository implements TrainerRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations jdbcOperations;
    private final TrainerRowMapper mapper;
    private final AllWorkoutOfTrainerDtoRowMapper allWorkoutDtoRowMapper;

    public TrainerDbRepository(JdbcTemplate jdbcTemplate, TrainerRowMapper mapper,
                               NamedParameterJdbcOperations jdbcOperations,
                               AllWorkoutOfTrainerDtoRowMapper allWorkoutDtoRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcOperations = jdbcOperations;
        this.allWorkoutDtoRowMapper = allWorkoutDtoRowMapper;
        this.mapper = mapper;
    }

    @Override
    public Trainer create(CreateTrainerRequest newTrainer) {
        log.info("Создаем нового тренера : {}", newTrainer.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", newTrainer.getName());
        params.addValue("surname", newTrainer.getSurname());
        params.addValue("birthday", newTrainer.getBirthday());
        params.addValue("telephone", newTrainer.getTelephone());
        params.addValue("email", newTrainer.getEmail());
        params.addValue("login", newTrainer.getLogin());

        String sql = "INSERT INTO trainer (name, surname, birthday, telephone, email, login) " +
                "VALUES (:name, :surname, :birthday, :telephone, :email, :login)";

        jdbcOperations.update(sql, params, keyHolder);

        log.info("Новый тренер {} создан", newTrainer.getName());

        Integer trainerId = Objects.requireNonNull(keyHolder.getKey()).intValue();

        return findTrainer(trainerId);
    }

    @Override
    public List<Trainer> findAllTrainers() {
        String sql = "SELECT * FROM trainer";
        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public Trainer findTrainer(Integer id) {
        String sql = "SELECT * FROM trainer WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, mapper, id);
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
    public List<WorkoutDto> findAllWorkoutsOfTrainer(Integer id) {

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        String sql = "SELECT CLIENT.name, CLIENT.surname, CLIENT.telephone,WORKOUT.id, WORKOUT.\"date\", " +
                "WORKOUT.start_time, WORKOUT.end_time " +
                "FROM WORKOUT " +
                "LEFT JOIN CLIENT ON WORKOUT.client_id = CLIENT.id " +
                "LEFT JOIN TRAINER ON WORKOUT.trainer_id = TRAINER.id " +
                "WHERE WORKOUT.trainer_id = :id";

        return jdbcOperations.query(sql, params, allWorkoutDtoRowMapper);
    }
}
