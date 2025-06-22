package com.example.trial_training.mapper.client;

import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.model.trainer.Trainer;
import com.example.trial_training.model.workout.Workout;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AllWorkoutOfClientDtoRowMapper implements RowMapper<WorkoutDto> {

    @Override
    public WorkoutDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        Trainer trainer = Trainer.builder()
                .name(rs.getString("name"))
                .surname(rs.getString("surname"))
                .telephone(rs.getString("telephone"))
                .build();

        Workout workout = Workout.builder()
                .id(rs.getInt("id"))
                .date(rs.getDate("date").toLocalDate())
                .startTime(rs.getTime("start_time").toLocalTime())
                .endTime(rs.getTime("end_time").toLocalTime())
                .build();

        return new WorkoutDto(workout, trainer);
    }
}
