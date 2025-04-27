package com.example.TrialTraining.trainingCalendar.repository;

import com.example.TrialTraining.trainingCalendar.model.TrainingCalendar;
import com.example.TrialTraining.workout.mapper.TrainingCalendarRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainerCalendarBdRepository implements TrainerCalendarRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TrainingCalendarRowMapper mapper;
    private final NamedParameterJdbcOperations jdbcOperations;

    public TrainerCalendarBdRepository(JdbcTemplate jdbcTemplate, TrainingCalendarRowMapper mapper, NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<TrainingCalendar> findAllTrainerWorkouts(Integer id) {
        //создаю sql запрос что бы получить тренировки тренера
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        String sql = "SELECT client.name, client.surname, workout.\"date\", workout.start_time " +
                "FROM client " +
                "LEFT JOIN workout ON client.id = workout.client_id WHERE trainer_id = :id";

        // выполняю запрос и получаю результат
        List<TrainingCalendar> allTrainings = jdbcOperations.query(sql, params, mapper);

        return allTrainings;
    }
}
