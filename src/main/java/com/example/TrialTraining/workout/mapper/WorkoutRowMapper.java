package com.example.TrialTraining.workout.mapper;

import com.example.TrialTraining.workout.dto.WorkoutDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkoutRowMapper implements RowMapper<WorkoutDto> {
    @Override
    public WorkoutDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return WorkoutDto.builder()
                .clientId(rs.getInt("clientId"))
                .trainerId(rs.getInt("trainerId"))
                .dateTime(rs.getTimestamp("dataTime").toLocalDateTime())
                .build();
    }
}
