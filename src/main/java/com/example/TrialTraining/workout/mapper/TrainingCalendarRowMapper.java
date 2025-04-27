package com.example.TrialTraining.workout.mapper;

import com.example.TrialTraining.trainingCalendar.model.TrainingCalendar;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class TrainingCalendarRowMapper implements RowMapper<TrainingCalendar> {
    @Override
    public TrainingCalendar mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TrainingCalendar.builder()
                .nameClient(rs.getString("name"))
                .surnameClient(rs.getString("surname"))
                .date(rs.getDate("date").toLocalDate())
                .startTime(rs.getTimestamp("start_time").toLocalDateTime().toLocalTime())
                .build();
    }
}
