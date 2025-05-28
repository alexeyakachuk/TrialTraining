package com.example.trial_training.mapper;

import com.example.trial_training.dto.AllWorkoutDto;
import com.example.trial_training.model.client.Client;
import com.example.trial_training.model.workout.Workout;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AllWorkoutDtoRowMapper implements RowMapper<AllWorkoutDto> {
    @Override
    public AllWorkoutDto mapRow(ResultSet rs, int rowNum) throws SQLException {

        Client client = Client.builder()
                .name(rs.getString("name"))
                .surname(rs.getString("surname"))
                .telephone(rs.getString("telephone"))
                .build();

        Workout workout = Workout.builder()
                .date(rs.getDate("date").toLocalDate())
                .startTime(rs.getTime("start_time").toLocalTime())
                .endTime(rs.getTime("end_time").toLocalTime())
                .build();

        return AllWorkoutDto.builder()
                .client(client)
                .workout(workout)
                .build();
    }
}
