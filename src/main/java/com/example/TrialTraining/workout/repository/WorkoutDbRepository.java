package com.example.TrialTraining.workout.repository;

import com.example.TrialTraining.workout.mapper.WorkoutRowMapper;
import com.example.TrialTraining.workout.model.Workout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
public class WorkoutDbRepository implements WorkoutRepository {

    private final JdbcTemplate jdbcTemplate;
    private final WorkoutRowMapper mapper;
    private final NamedParameterJdbcOperations jdbcOperations;

    public WorkoutDbRepository(JdbcTemplate jdbcTemplate, WorkoutRowMapper mapper, NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Workout create(Integer clientId, Integer trainerId, LocalDateTime dateTime) {
        String sql = "INSERT INTO workout (client_id, trainer_id, data_time) " +
                "VALUES (:client_id, :trainer_id, :data_time)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("client_id", clientId);
        params.addValue("trainer_id", trainerId);
        params.addValue("data_time", dateTime);
        jdbcOperations.update(sql, params, keyHolder);

        Integer workoutId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return findWorkout(workoutId);
    }

    @Override
    public Workout findWorkout(Integer id) {
        String sql = "SELECT * FROM workout WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, mapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }
}
