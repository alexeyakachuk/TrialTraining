package com.example.TrialTraining.trainer.mapper;

import com.example.TrialTraining.trainer.trainerDto.TrainerDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class TrainerRowMapper implements RowMapper<TrainerDto> {
    @Override
    public TrainerDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TrainerDto.builder()
                .name(rs.getString("name"))
                .surname(rs.getString("surname"))
                .birthday(rs.getTimestamp("birthday").toLocalDateTime().toLocalDate())
                .telephone(rs.getString("telephone"))
                .email(rs.getString("email"))
                .workout(new HashSet<>())
                .build();
    }
}
