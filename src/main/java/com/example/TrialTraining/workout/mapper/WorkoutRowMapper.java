package com.example.TrialTraining.workout.mapper;

import com.example.TrialTraining.workout.model.Workout;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class WorkoutRowMapper implements RowMapper<Workout> {
    @Override
    public Workout mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Workout.builder()
                .id(rs.getInt("id"))
                .clientId(rs.getInt("client_Id"))
                .trainerId(rs.getInt("trainer_Id"))
                .date(rs.getDate("date").toLocalDate())
                .startTime(rs.getTimestamp("start_Time").toLocalDateTime().toLocalTime())
                .endTime(rs.getTimestamp("end_Time").toLocalDateTime().toLocalTime())
                .build();
    }
}
