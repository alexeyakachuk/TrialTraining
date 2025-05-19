package com.example.trial_training.repository.workout;

import com.example.trial_training.mapper.workout.WorkoutRowMapper;
import com.example.trial_training.model.workout.Workout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
    public Workout create(Workout newWorkout) {
        String sql = "INSERT INTO workout (client_id, trainer_id, \"date\", start_time, end_time) " +
                "VALUES (:client_id, :trainer_id, :date, :start_time, :end_time)";

        LocalTime endTime = newWorkout.getStartTime().plusHours(1);
        log.info("Создаем новую тренировку");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("client_id", newWorkout.getClientId());
        params.addValue("trainer_id", newWorkout.getTrainerId());
        params.addValue("date", newWorkout.getDate());
        params.addValue("start_time", newWorkout.getStartTime());
        params.addValue("end_time", endTime);
        jdbcOperations.update(sql, params, keyHolder);

        Integer workoutId = Objects.requireNonNull(keyHolder.getKey()).intValue();

        log.info("Новая тренировка {} создана", workoutId);

        return findWorkout(workoutId);
    }

    @Override
    public Workout findWorkout(Integer id) {
        String sql = "SELECT * FROM workout WHERE id = ?";
//        String sql = "SELECT * FROM workout WHERE client_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, mapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    @Override
    public List<Workout> findAllWorkouts() {
        String sql = "SELECT * FROM workout";
        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public void deleteWorkout(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        String sql = "DELETE FROM workout WHERE id = :id";

        jdbcOperations.update(sql, params);
    }

    //времменый метод для проверки создания тренировки
    private Workout findNewCreateWorkout(Integer trainerId, LocalDate date, LocalTime startTime, Integer clientId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("trainer_id", trainerId);
        params.addValue("date", date);
        params.addValue("start_time", startTime);
        params.addValue("client_id", clientId);

        String sql = "SELECT * FROM workout WHERE trainer_id = :trainer_id AND \"date\" = :date " +
                "AND start_time = :start_time AND client_id = :client_id";

        return jdbcOperations.queryForObject(sql, params, mapper);
    }
}
